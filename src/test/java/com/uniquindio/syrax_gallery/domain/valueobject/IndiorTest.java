package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IndiorTest {
    @Test
    void testDescripcionObligatoria() {
        Exception exNull = assertThrows(ReglaDominioException.class, () -> new Indior(null));
        assertEquals("La descripción es obligatorio.", exNull.getMessage());

        Exception exVacio = assertThrows(ReglaDominioException.class, () -> new Indior(""));
        assertEquals("La descripción es obligatorio.", exVacio.getMessage());

        Exception exEspacios = assertThrows(ReglaDominioException.class, () -> new Indior("   "));
        assertEquals("La descripción es obligatorio.", exEspacios.getMessage());
    }

    @Test
    void testCaracteresNoPermitidos() {
        Exception exCaracter = assertThrows(ReglaDominioException.class, () ->
                new Indior("Descripción con caracteres prohibidos @")
        );
        assertEquals("La descripción contiene caracteres no permitidos.", exCaracter.getMessage());
    }

    @Test
    void testLongitudMinima() {
        Exception exCorta = assertThrows(ReglaDominioException.class, () -> new Indior("Corta"));
        assertEquals("La especialidad debe tener al menos 10 caracteres", exCorta.getMessage());
    }

    @Test
    void testLongitudMaxima() {
        String textoLargo = "A".repeat(301);
        Exception exLarga = assertThrows(ReglaDominioException.class, () -> new Indior(textoLargo));
        assertEquals("La especialidad no puede superar 300 caracteres", exLarga.getMessage());
    }

    @Test
    void testCoincideCon() {
        Indior indior = new Indior("Especialidad Médica");

        assertTrue(indior.coincideCon("  ESPECIALIDAD MÉDICA  "));
        assertFalse(indior.coincideCon("Otra descripción"));
        assertFalse(indior.coincideCon(null));
    }
}
