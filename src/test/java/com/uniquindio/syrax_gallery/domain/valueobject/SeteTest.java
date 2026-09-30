package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SeteTest {

    @Test
    void testSeteValido() {

        String nombre = "Noche Estrellada";

        Sete sete = new Sete(nombre);

        assertNotNull(sete);
        assertEquals(nombre, sete.nombre());
    }

    @Test
    void testSeteConTildesEnieYDieresis() {
        String nombre = "Niño Añejo Pingüino Él";

        Sete sete = new Sete(nombre);

        assertEquals(nombre, sete.nombre());
    }

    @Test
    void testSeteConNumeros() {
        String nombre = "Obra 2024";

        Sete sete = new Sete(nombre);

        assertEquals(nombre, sete.nombre());
    }

    @Test
    void testSeteNulo() {
        String nombre = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Sete(nombre));

        assertEquals("El nombre de la obra es obligatorio", ex.getMessage());
    }

    @Test
    void testSeteVacio() {
        String nombre = "";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Sete(nombre));

        assertEquals("El nombre de la obra es obligatorio", ex.getMessage());
    }

    @Test
    void testSeteSoloEspacios() {
        String nombre = "     ";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Sete(nombre));

        assertEquals("El nombre de la obra es obligatorio", ex.getMessage());
    }

    @Test
    void testSeteConSignosDePuntuacion() {
        String nombre = "Retrato, 1889 - Noche Estrellada";

        Sete sete = new Sete(nombre);

        assertEquals(nombre, sete.nombre());
    }

    @Test
    void testSeteMenorAlMinimo() {
        String nombre = "ab";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Sete(nombre));

        assertEquals("El nombre de la obra debe tener al menos 3 caracteres", ex.getMessage());
    }

    @Test
    void testSeteLosEspaciosExtremosNoCuentanParaElMinimo() {
        String nombre = " ab ";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Sete(nombre));

        assertEquals("El nombre de la obra debe tener al menos 3 caracteres", ex.getMessage());
    }

    @Test
    void testSeteEnElLimiteMinimo() {
        String nombre = "abc";

        Sete sete = new Sete(nombre);

        assertEquals(nombre, sete.nombre());
    }

    @Test
    void testSeteEnElLimiteMaximo() {
        String nombre = "a".repeat(100);

        Sete sete = new Sete(nombre);

        assertEquals(100, sete.nombre().length());
    }

    @Test
    void testSeteSuperaElMaximo() {
        String nombre = "a".repeat(101);

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Sete(nombre));

        assertEquals("El nombre de la obra no puede superar 100 caracteres", ex.getMessage());
    }

    @Test
    void testSeteIgualdadPorValor() {
        Sete primero = new Sete("Luna Roja");
        Sete segundo = new Sete("Luna Roja");
        Sete distinto = new Sete("Luna Azul");

        boolean iguales = primero.equals(segundo);
        boolean diferentes = primero.equals(distinto);

        assertTrue(iguales);
        assertEquals(primero.hashCode(), segundo.hashCode());
        assertFalse(diferentes);
    }
}
