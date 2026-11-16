package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearSunfyreUseCaseTest {

    @Mock
    private SunfyreRepository sunfyreRepository;

    @Mock
    private AzantysRepository azantysRepository;

    @InjectMocks
    private CrearSunfyreUseCase useCase;

    private Sunfyre crear(String azantysId) {
        return useCase.ejecutar("sf-001", azantysId,
                new Sete("Tatuaje realista"),
                new Jorva("Tatuaje realista en blanco y negro, incluye el diseño."),
                new Urnebion("https://cdn.syrax.com/servicios/sf-001.jpg"),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(300000, "COP"),
                180,
                new Jedar(EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                        LocalTime.of(9, 0), LocalTime.of(18, 0)));
    }

    @Test
    void deberiaGuardarLaSunfyreDelAzantys() {
        // Arrange
        when(azantysRepository.obtener("az-001")).thenReturn(Optional.of(mock(Azantys.class)));

        // Act
        Sunfyre sunfyre = crear("az-001");

        // Assert
        assertTrue(sunfyre.isActivo());
        assertEquals("az-001", sunfyre.getIdAzantys());
        verify(sunfyreRepository).guardar(sunfyre);
    }

    @Test
    void noDeberiaCrearSiElAzantysNoExiste() {
        // Arrange
        when(azantysRepository.obtener("az-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> crear("az-999"));
        verify(sunfyreRepository, never()).guardar(any());
    }
}
