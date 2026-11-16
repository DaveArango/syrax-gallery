package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
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
class CancelarDohaeragonUseCaseTest {

    @Mock
    private DohaeragonRepository dohaeragonRepository;

    @Mock
    private KelitisRepository kelitisRepository;

    @InjectMocks
    private CancelarDohaeragonUseCase useCase;

    private Dohaeragon dohaeragonAbonada() {
        Dohaeragon dohaeragon = Dohaeragon.solicitar("dh-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
        dohaeragon.confirmar();
        return dohaeragon;
    }

    @Test
    void deberiaCancelarLaDohaeragonYReembolsarSuKelitis() {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonAbonada();
        Kelitis kelitis = Kelitis.realizar("kl-001", "dh-001", "az-001", "ze-001", new Vala(150000, "COP"));
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragon));
        when(kelitisRepository.obtenerPorDohaeragon("dh-001")).thenReturn(Optional.of(kelitis));

        // Act
        Dohaeragon resultado = useCase.ejecutar("dh-001", "ze-001", "El cliente viaja esa semana");

        // Assert
        assertEquals(DohaeroxJeda.CANCELADO, resultado.getDohaeroxJeda());
        assertEquals(Gelior.REEMBOLSADO, kelitis.getGelior());
        verify(kelitisRepository).guardar(kelitis);
        verify(dohaeragonRepository).guardar(dohaeragon);
    }

    @Test
    void noDeberiaCancelarUnaDohaeragonQueYaInicio() {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonAbonada();
        dohaeragon.iniciarEjecucion();
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragon));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("dh-001", "ze-001", "Ya no la quiero"));

        assertEquals(DohaeroxJeda.EN_EJECUCION, dohaeragon.getDohaeroxJeda());
        verify(dohaeragonRepository, never()).guardar(any());
        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaPermitirQueUnTerceroCancele() {
        // Arrange
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragonAbonada()));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("dh-001", "ze-999", "No es mía"));

        verify(dohaeragonRepository, never()).guardar(any());
    }

    @Test
    void deberiaLanzarExcepcionCuandoLaDohaeragonNoExiste() {
        // Arrange
        when(dohaeragonRepository.obtener("no-existe")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> useCase.ejecutar("no-existe", "ze-001", "Motivo"));

        verify(dohaeragonRepository, never()).guardar(any());
    }
}
