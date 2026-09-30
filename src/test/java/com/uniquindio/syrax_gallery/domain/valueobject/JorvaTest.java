package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JorvaTest {

    @Test
    void testJorvaValida() {
        String descripcion = "Paisaje al atardecer pintado con técnica de óleo";

        Jorva jorva = new Jorva(descripcion);

        assertNotNull(jorva);
        assertEquals(descripcion, jorva.descripcion());
    }

    @Test
    void testJorvaNula() {
        String descripcion = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Jorva(descripcion));

        assertEquals("La descripción de la obra es obligatoria", ex.getMessage());
    }

    @Test
    void testJorvaVacia() {
        String descripcion = "";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Jorva(descripcion));

        assertEquals("La descripción de la obra es obligatoria", ex.getMessage());
    }

    @Test
    void testJorvaSoloEspacios() {
        String descripcion = "          ";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Jorva(descripcion));

        assertEquals("La descripción de la obra es obligatoria", ex.getMessage());
    }

    @Test
    void testJorvaMenorAlMinimo() {
        String descripcion = "corta";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Jorva(descripcion));

        assertEquals("La descripción de la obra debe tener al menos 10 caracteres", ex.getMessage());
    }

    @Test
    void testJorvaEnElLimiteMinimo() {
        String descripcion = "a".repeat(10);

        Jorva jorva = new Jorva(descripcion);

        assertEquals(10, jorva.descripcion().length());
    }

    @Test
    void testJorvaEnElLimiteMaximo() {
        String descripcion = "a".repeat(1000);

        Jorva jorva = new Jorva(descripcion);

        assertEquals(1000, jorva.descripcion().length());
    }

    @Test
    void testJorvaSuperaElMaximo() {
        String descripcion = "a".repeat(1001);

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> new Jorva(descripcion));

        assertEquals("La descripción de la obra no puede superar 1000 caracteres", ex.getMessage());
    }
}
