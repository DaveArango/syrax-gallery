package com.uniquindio.syrax_gallery.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KanezTest {

    @Test
    void testEnumValido() {
        Kanez borrador = Kanez.BORRADOR;
        Kanez exhibicion = Kanez.EXHIBICION;
        Kanez enVenta = Kanez.EN_VENTA;
        Kanez vendido = Kanez.VENDIDO;
        Kanez retirado = Kanez.RETIRADO;

        assertNotNull(borrador);
        assertEquals("BORRADOR", borrador.name());
        assertEquals("EXHIBICION", exhibicion.name());
        assertEquals("EN_VENTA", enVenta.name());
        assertEquals("VENDIDO", vendido.name());
        assertEquals("RETIRADO", retirado.name());
    }

    @Test
    void testCantidadDeEstados() {
        int esperados = 5;

        Kanez[] valores = Kanez.values();

        assertEquals(esperados, valores.length);
    }

    @Test
    void testValueOfValido() {
        String nombre = "EN_VENTA";

        Kanez kanez = Kanez.valueOf(nombre);

        assertEquals(Kanez.EN_VENTA, kanez);
    }

    @Test
    void testValueOfInvalido() {
        String nombre = "INEXISTENTE";

        assertThrows(IllegalArgumentException.class, () -> Kanez.valueOf(nombre));
    }
}
