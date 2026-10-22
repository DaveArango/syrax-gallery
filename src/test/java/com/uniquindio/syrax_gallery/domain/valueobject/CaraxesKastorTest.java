package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CaraxesKastorTest {
    @Test
    void testDesdeConValorValido() {
        String valor = "PINTURA";

        CaraxesKastor kastor = CaraxesKastor.desde(valor);

        assertEquals(CaraxesKastor.PINTURA, kastor);
    }

    @Test
    void testDesdeConValorEnMinusculas() {
        String valor = "escultura";

        CaraxesKastor kastor = CaraxesKastor.desde(valor);

        assertEquals(CaraxesKastor.ESCULTURA, kastor);
    }

    @Test
    void testDesdeConValorInvalido() {
        String valor = "XYZ";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> CaraxesKastor.desde(valor));

        assertTrue(ex.getMessage().contains("XYZ"));
    }

    @Test
    void testDesdeConValorNulo() {
        String valor = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> CaraxesKastor.desde(valor));

        assertEquals("El kastor de la obra es obligatorio", ex.getMessage());
    }

    @Test
    void testDesdeConValorVacio() {
        String valor = "   ";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> CaraxesKastor.desde(valor));

        assertEquals("El kastor de la obra es obligatorio", ex.getMessage());
    }
}
