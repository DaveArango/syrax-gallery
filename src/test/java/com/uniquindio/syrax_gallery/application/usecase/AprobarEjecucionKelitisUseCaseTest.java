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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AprobarEjecucionKelitisUseCaseTest {

    @Mock
    private KelitisRepository kelitisRepository;

    @Mock
    private SunfyreRepository sunfyreRepository;

    @InjectMocks
    private AprobarEjecucionKelitisUseCase useCase;

    private Sunfyre sunfyreAbonada() {
        Sunfyre sunfyre = Sunfyre.solicitar("sf-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
        sunfyre.confirmar();
        return sunfyre;
    }

    private Kelitis kelitisRetenido() {
        return Kelitis.realizar("kl-001", "sf-001", "az-001", "ze-001", new Vala(150000, "COP"));
    }

    @Test
    void deberiaLiberarElPagoCuandoElServicioTermino() {
        // Arrange
        Kelitis kelitis = kelitisRetenido();
        Sunfyre sunfyre = sunfyreAbonada();
        sunfyre.iniciarEjecucion();
        sunfyre.finalizar();
        when(kelitisRepository.obtener("kl-001")).thenReturn(Optional.of(kelitis));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyre));

        // Act
        Kelitis resultado = useCase.ejecutar("kl-001", "ze-001");

        // Assert
        assertEquals(Gelior.LIBERADO, resultado.getGelior());
        verify(kelitisRepository).guardar(kelitis);
    }

    @Test
    void noDeberiaLiberarElPagoSiElServicioNoHaTerminado() {
        // Arrange
        Kelitis kelitis = kelitisRetenido();
        when(kelitisRepository.obtener("kl-001")).thenReturn(Optional.of(kelitis));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyreAbonada()));

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("kl-001", "ze-001"));

        assertEquals("Solo se puede aprobar un servicio ya ejecutado", ex.getMessage());
        assertEquals(Gelior.RETENIDO, kelitis.getGelior());
        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaPermitirQueOtroZentysApruebe() {
        // Arrange
        when(kelitisRepository.obtener("kl-001")).thenReturn(Optional.of(kelitisRetenido()));

        // Act & Assert
        assertThrows(ReglaDominioException.class, () -> useCase.ejecutar("kl-001", "ze-999"));

        verify(kelitisRepository, never()).guardar(any());
    }
}
