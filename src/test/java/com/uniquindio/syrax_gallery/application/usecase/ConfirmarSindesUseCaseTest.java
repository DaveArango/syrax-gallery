package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmarSindesUseCaseTest {

    @Mock
    private SindesRepository sindesRepository;

    @Mock
    private CaraxesRepository caraxesRepository;

    private ConfirmarSindesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConfirmarSindesUseCase(sindesRepository, caraxesRepository);
    }

    private Sindes sindesPendiente() {
        return Sindes.realizar("sd-001", "cx-004", "az-001", "zentys-01", new Vala(450000, "COP"));
    }

    private Caraxes caraxesEnVenta() {
        return Caraxes.reconstruir("cx-004", "az-001", new Vala(450000, "COP"),
                new Sete("Calles de Salento"),
                new Jorva("Grabado en linoleo con tinta negra sobre papel artesanal."),
                new Urnebion("https://cdn.syrax.com/obras/cx-004.jpg"),
                CaraxesKastor.GRABADO, Kanez.EN_VENTA);
    }

    @Test
    void deberiaCompletarElSindesYDejarLaObraVendida() {
        // Arrange
        Sindes sindes = sindesPendiente();
        Caraxes caraxes = caraxesEnVenta();
        when(sindesRepository.obtener("sd-001")).thenReturn(Optional.of(sindes));
        when(caraxesRepository.obtener("cx-004")).thenReturn(Optional.of(caraxes));

        // Act
        useCase.ejecutar("sd-001", "az-001");

        // Assert
        assertEquals(SindesJeda.COMPLETADA, sindes.getSindesJeda());
        assertEquals(Kanez.VENDIDO, caraxes.getKanez());
        verify(sindesRepository).guardar(sindes);
        verify(caraxesRepository).guardar(caraxes);
    }

    @Test
    void noDeberiaConfirmarOtroAzantys() {
        // Arrange
        when(sindesRepository.obtener("sd-001")).thenReturn(Optional.of(sindesPendiente()));

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("sd-001", "az-002"));

        assertEquals("Solo el Azantys de la obra puede confirmar este Sindes", ex.getMessage());
        verify(sindesRepository, never()).guardar(any());
        verify(caraxesRepository, never()).guardar(any());
    }
}
