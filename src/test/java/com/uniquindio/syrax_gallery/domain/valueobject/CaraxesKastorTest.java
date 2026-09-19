package com.uniquindio.syrax_gallery.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CaraxesKastorTest {
    @Test
    void testEnumValido() {
        CaraxesKastor pintura = CaraxesKastor.PINTURA;
        CaraxesKastor escultura = CaraxesKastor.ESCULTURA;

        assertNotNull(pintura);
        assertEquals("PINTURA", pintura.name());
        assertEquals("ESCULTURA", escultura.name());
    }
}
