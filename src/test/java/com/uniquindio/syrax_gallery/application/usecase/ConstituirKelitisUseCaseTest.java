package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
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
    private DohaeragonRepository dohaeragonRepository;

    private ConstituirKelitisUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConstituirKelitisUseCase(kelitisRepository, dohaeragonRepository,
                new VerificadorDeAgenda(dohaeragonRepository), new VerificadorDeKelitis(kelitisRepository));
    }

    private Dohaeragon dohaeragonPendiente() {
        return Dohaeragon.solicitar("dh-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
    }

    @Test
    void deberiaRetenerElPagoYDejarLaDohaeragonAbonada() {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonPendiente();
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragon));

        // Act
        Kelitis kelitis = useCase.ejecutar("kl-001", "dh-001", "ze-001");

        // Assert
        assertEquals(Gelior.RETENIDO, kelitis.getGelior());
        assertEquals(DohaeroxJeda.ABONADO, dohaeragon.getDohaeroxJeda());
        assertEquals(dohaeragon.getValaCongelado(), kelitis.getValaCongelado());
        verify(dohaeragonRepository).guardar(dohaeragon);
        verify(kelitisRepository).guardar(kelitis);
    }

    @Test
    void noDeberiaPermitirQueOtroZentysPague() {
        // Arrange
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragonPendiente()));

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("kl-001", "dh-001", "ze-999"));

        assertEquals("Solo el Zentys de la Dohaeragon puede constituir su Kelitis", ex.getMessage());
        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaConstituirUnSegundoKelitisParaLaMismaDohaeragon() {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonPendiente();
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragon));
        when(kelitisRepository.existePorDohaeragon("dh-001")).thenReturn(true);

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("kl-002", "dh-001", "ze-001"));

        assertEquals(DohaeroxJeda.PENDIENTE, dohaeragon.getDohaeroxJeda());
        verify(kelitisRepository, never()).guardar(any());
        verify(dohaeragonRepository, never()).guardar(any());
    }
}
