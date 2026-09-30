package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UrnebionTest {

    @Test
    void testUrnebionValido() {
        String url = "https://cdn.syrax.com/obras/paisaje.jpg";

        Urnebion urnebion = new Urnebion(url);

        assertNotNull(urnebion);
        assertEquals(url, urnebion.url());
    }

    @Test
    void testUrnebionNulo() {
        String url = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Urnebion(url));

        assertEquals("La imagen de referencia de la obra física es obligatoria", ex.getMessage());
    }

    @Test
    void testUrnebionVacio() {
        String url = "";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Urnebion(url));

        assertEquals("La imagen de referencia de la obra física es obligatoria", ex.getMessage());
    }

    @Test
    void testUrnebionSoloEspacios() {
        String url = "   ";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Urnebion(url));

        assertEquals("La imagen de referencia de la obra física es obligatoria", ex.getMessage());
    }

    @Test
    void testUrnebionIgualdadPorValor() {
        Urnebion primero = new Urnebion("https://cdn.syrax.com/obras/a.jpg");
        Urnebion segundo = new Urnebion("https://cdn.syrax.com/obras/a.jpg");
        Urnebion distinto = new Urnebion("https://cdn.syrax.com/obras/b.jpg");

        boolean iguales = primero.equals(segundo);
        boolean diferentes = primero.equals(distinto);

        assertTrue(iguales);
        assertEquals(primero.hashCode(), segundo.hashCode());
        assertFalse(diferentes);
    }
}
