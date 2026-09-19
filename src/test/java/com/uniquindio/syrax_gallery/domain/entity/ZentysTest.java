package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Indior;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ZentysTest {
    private final Runiapos runiaposValido = new Runiapos("contacto@zentys.com");
    private final Indior indiorValido = new Indior("Descripción válida de la especialidad.");

    @Test
    void testIdObligatorio() {
        Exception exNullId = assertThrows(ReglaDominioException.class, () ->
                Zentys.crear(null, "Zentys Alpha", runiaposValido, indiorValido)
        );
        assertEquals("El id del Zentys es obligatorio", exNullId.getMessage());
    }

    @Test
    void testNombreObligatorio() {
        Exception exNullNombre = assertThrows(ReglaDominioException.class, () ->
                Zentys.crear("123", null, runiaposValido, indiorValido)
        );
        assertEquals("El nombre del Zentys es obligatorio.", exNullNombre.getMessage());

        Exception exVacioNombre = assertThrows(ReglaDominioException.class, () ->
                Zentys.crear("123", "", runiaposValido, indiorValido)
        );
        assertEquals("El nombre del Zentys es obligatorio.", exVacioNombre.getMessage());

        Exception exEspaciosNombre = assertThrows(ReglaDominioException.class, () ->
                Zentys.crear("123", "   ", runiaposValido, indiorValido)
        );
        assertEquals("El nombre del Zentys es obligatorio.", exEspaciosNombre.getMessage());
    }

    @Test
    void testNombreConCaracteresEspeciales() {
        Exception exEspeciales = assertThrows(ReglaDominioException.class, () ->
                Zentys.crear("123", "Zentys_@lpha", runiaposValido, indiorValido)
        );
        assertEquals("El nombre del Zentys no puede contener caracteres especiales.", exEspeciales.getMessage());
    }

    @Test
    void testCrearZentysValido() {
        Zentys zentys = Zentys.crear("123", "Zentys Alpha 01", runiaposValido, indiorValido);
        assertNotNull(zentys);
    }
}
