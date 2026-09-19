package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RuniaposTest {
    @Test
    void testEmailObligatorio() {
        Exception exNull = assertThrows(ReglaDominioException.class, () -> new Runiapos(null));
        assertEquals("El email es obligatorio", exNull.getMessage());

        Exception exVacio = assertThrows(ReglaDominioException.class, () -> new Runiapos(""));
        assertEquals("El email es obligatorio", exVacio.getMessage());

        Exception exEspacios = assertThrows(ReglaDominioException.class, () -> new Runiapos("   "));
        assertEquals("El email es obligatorio", exEspacios.getMessage());
    }

    @Test
    void testFaltaArrobaOPunto() {
        Exception exSinArroba = assertThrows(ReglaDominioException.class, () -> new Runiapos("://dominio.com"));
        assertEquals("Email inválido", exSinArroba.getMessage());

        Exception exSinPunto = assertThrows(ReglaDominioException.class, () -> new Runiapos("usuario@dominio"));
        assertEquals("Email inválido", exSinPunto.getMessage());
    }

    @Test
    void testArrobaEnExtremos() {
        Exception exEmpiezaConArroba = assertThrows(ReglaDominioException.class, () -> new Runiapos("@dominio.com"));
        assertEquals("Email inválido", exEmpiezaConArroba.getMessage());

        Exception exTerminaConArroba = assertThrows(ReglaDominioException.class, () -> new Runiapos("usuario.com@"));
        assertEquals("Email inválido", exTerminaConArroba.getMessage());
    }

    @Test
    void testRuniaposValido() {
        Runiapos runiapos = new Runiapos("correo@ejemplo.com");
        assertNotNull(runiapos);
        assertEquals("correo@ejemplo.com", runiapos.direccion());
    }
}
