package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeKelitis;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConstituirKelitisUseCaseTest {

    @Mock
    private KelitisRepository kelitisRepository;

    @Mock
    private SunfyreRepository sunfyreRepository;

    private ConstituirKelitisUseCase useCase;

    @BeforeEach
    void setUp() {
        // Los servicios de dominio son reales (no simulados): así la prueba ejercita las reglas de verdad
        useCase = new ConstituirKelitisUseCase(kelitisRepository, sunfyreRepository,
                new VerificadorDeAgenda(sunfyreRepository), new VerificadorDeKelitis(kelitisRepository));
    }

    private Sunfyre sunfyrePendiente() {
        return Sunfyre.solicitar("sf-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
    }

    @Test
    void deberiaRetenerElPagoYDejarLaSunfyreAbonada() {
        // Arrange
        Sunfyre sunfyre = sunfyrePendiente();
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyre));

        // Act
        Kelitis kelitis = useCase.ejecutar("kl-001", "sf-001", "ze-001");

        // Assert
        assertEquals(Gelior.RETENIDO, kelitis.getGelior());
        assertEquals(DohaeroxJeda.ABONADO, sunfyre.getDohaeroxJeda());
        assertEquals(sunfyre.getValaCongelado(), kelitis.getValaCongelado());
        verify(sunfyreRepository).guardar(sunfyre);
        verify(kelitisRepository).guardar(kelitis);
    }

    @Test
    void noDeberiaPermitirQueOtroZentysPague() {
        // Arrange
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyrePendiente()));

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("kl-001", "sf-001", "ze-999"));

        assertEquals("Solo el Zentys de la Sunfyre puede constituir su Kelitis", ex.getMessage());
        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaConstituirUnSegundoKelitisParaLaMismaSunfyre() {
        // Arrange
        Sunfyre sunfyre = sunfyrePendiente();
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyre));
        when(kelitisRepository.existePorSunfyre("sf-001")).thenReturn(true);

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("kl-002", "sf-001", "ze-001"));

        assertEquals(DohaeroxJeda.PENDIENTE, sunfyre.getDohaeroxJeda());
        verify(kelitisRepository, never()).guardar(any());
        verify(sunfyreRepository, never()).guardar(any());
    }
}
