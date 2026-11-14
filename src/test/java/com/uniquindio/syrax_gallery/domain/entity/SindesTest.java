package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.SindesJeda;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SindesTest {

    private Sindes sindesPendiente() {
        return Sindes.realizar("sd-001", "cx-004", "az-001", "zentys-01", new Vala(450000, "COP"));
    }

    @Test
    void deberiaNacerPendienteYConElValaCongelado() {
        // Arrange
        Vala valaDeLaObra = new Vala(450000, "COP");

        // Act
        Sindes sindes = Sindes.realizar("sd-001", "cx-004", "az-001", "zentys-01", valaDeLaObra);

        // Assert
        assertEquals(SindesJeda.PENDIENTE, sindes.getSindesJeda());
        assertEquals(valaDeLaObra, sindes.getValaCongelado());
        assertEquals("cx-004", sindes.getIdCaraxes());
        assertNotNull(sindes.getFecha());
    }

    @Test
    void noDeberiaRealizarseConUnValaEnCero() {
        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Sindes.realizar("sd-001", "cx-005", "az-001", "zentys-01", new Vala(0, "COP")));

        assertEquals("El Sindes debe tener un Vala mayor a cero", ex.getMessage());
    }

    @Test
    void deberiaConfirmarseUnSindesPendiente() {
        // Arrange
        Sindes sindes = sindesPendiente();

        // Act
        sindes.confirmar();

        // Assert
        assertEquals(SindesJeda.COMPLETADA, sindes.getSindesJeda());
    }

    @Test
    void noDeberiaConfirmarseDosVeces() {
        // Arrange
        Sindes sindes = sindesPendiente();
        sindes.confirmar();

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class, sindes::confirmar);
        assertEquals("Solo un Sindes PENDIENTE puede confirmarse", ex.getMessage());
    }

    @Test
    void deberiaCancelarseUnSindesPendiente() {
        // Arrange
        Sindes sindes = sindesPendiente();

        // Act
        sindes.cancelar();

        // Assert
        assertEquals(SindesJeda.CANCELADA, sindes.getSindesJeda());
    }

    @Test
    void noDeberiaCancelarseUnSindesCompletado() {
        // Arrange
        Sindes sindes = sindesPendiente();
        sindes.confirmar();

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class, sindes::cancelar);
        assertEquals("Solo un Sindes PENDIENTE puede cancelarse", ex.getMessage());
        assertEquals(SindesJeda.COMPLETADA, sindes.getSindesJeda());
    }

    @Test
    void soloElAzantysDeLaObraPuedeConfirmar() {
        // Arrange
        Sindes sindes = sindesPendiente();

        // Act & Assert
        assertDoesNotThrow(() -> sindes.validarAzantys("az-001"));
        assertThrows(ReglaDominioException.class, () -> sindes.validarAzantys("az-002"));
    }

    @Test
    void soloElZentysQueComproPuedeCancelar() {
        // Arrange
        Sindes sindes = sindesPendiente();

        // Act & Assert
        assertDoesNotThrow(() -> sindes.validarZentys("zentys-01"));
        assertThrows(ReglaDominioException.class, () -> sindes.validarZentys("zentys-02"));
    }
}
