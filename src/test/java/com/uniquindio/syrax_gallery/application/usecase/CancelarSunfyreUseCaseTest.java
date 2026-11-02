package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarSunfyreUseCaseTest {

    @Mock
    private SunfyreRepository sunfyreRepository;

    @Mock
    private KelitisRepository kelitisRepository;

    @InjectMocks
    private CancelarSunfyreUseCase useCase;

    private Sunfyre sunfyreAbonada() {
        Sunfyre sunfyre = Sunfyre.solicitar("sf-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
        sunfyre.confirmar();
        return sunfyre;
    }

    @Test
    void deberiaCancelarLaSunfyreYReembolsarSuKelitis() {
        // Arrange
        Sunfyre sunfyre = sunfyreAbonada();
        Kelitis kelitis = Kelitis.realizar("kl-001", "sf-001", "az-001", "ze-001", new Vala(150000, "COP"));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyre));
        when(kelitisRepository.obtenerPorSunfyre("sf-001")).thenReturn(Optional.of(kelitis));

        // Act
        Sunfyre resultado = useCase.ejecutar("sf-001", "ze-001", "El cliente viaja esa semana");

        // Assert
        assertEquals(DohaeroxJeda.CANCELADO, resultado.getDohaeroxJeda());
        assertEquals(Gelior.REEMBOLSADO, kelitis.getGelior());
        verify(kelitisRepository).guardar(kelitis);
        verify(sunfyreRepository).guardar(sunfyre);
    }

    @Test
    void noDeberiaCancelarUnaSunfyreQueYaInicio() {
        // Arrange
        Sunfyre sunfyre = sunfyreAbonada();
        sunfyre.iniciarEjecucion();
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyre));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("sf-001", "ze-001", "Ya no la quiero"));

        assertEquals(DohaeroxJeda.EN_EJECUCION, sunfyre.getDohaeroxJeda());
        verify(sunfyreRepository, never()).guardar(any());
        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaPermitirQueUnTerceroCancele() {
        // Arrange
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyreAbonada()));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("sf-001", "ze-999", "No es mía"));

        verify(sunfyreRepository, never()).guardar(any());
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaSunfyreNoExiste() {
        // Arrange
        when(sunfyreRepository.obtener("no-existe")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> useCase.ejecutar("no-existe", "ze-001", "Motivo"));

        verify(sunfyreRepository, never()).guardar(any());
    }
}
