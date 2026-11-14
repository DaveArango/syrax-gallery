package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GidaTest {

    @Test
    void deberiaCrearseConElTextoSinEspaciosSobrantes() {
        // Act
        Gida gida = Gida.crear("gd-001", "zentys-02", "cx-006", "  Hermoso retrato  ");

        // Assert
        assertEquals("Hermoso retrato", gida.getTexto());
        assertEquals("zentys-02", gida.getIdZentys());
        assertEquals("cx-006", gida.getIdCaraxes());
        assertNotNull(gida.getFecha());
    }

    @Test
    void noDeberiaCrearseVacio() {
        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Gida.crear("gd-001", "zentys-02", "cx-006", "   "));

        assertEquals("El Gida no puede estar vacío", ex.getMessage());
    }

    @Test
    void noDeberiaSuperarLos500Caracteres() {
        // Arrange
        String textoLargo = "a".repeat(501);

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> Gida.crear("gd-001", "zentys-02", "cx-006", textoLargo));
    }
}
