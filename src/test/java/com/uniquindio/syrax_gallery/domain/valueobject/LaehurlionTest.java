package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LaehurlionTest {

    @Test
    void crearLaehurlionValidaConUrlNoVacia() {
        String url = "https://cdn.syraxgallery.com/perfil/azantys123.png";

        Laehurlion foto = new Laehurlion(url);

        assertEquals(url, foto.url());
    }

    @Test
    void deberiaFallarSiLaUrlEsNula() {
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> new Laehurlion(null));

        assertEquals("La foto de perfil es obligatoria", ex.getMessage());
    }

    @Test
    void deberiaFallarSiLaUrlEstaVacia() {
        String url = "";

        assertThrows(ReglaDominioException.class, () -> new Laehurlion(url));
    }

    @Test
    void deberiaFallarSiLaUrlEstaEnBlanco() {
        String url = "   ";

        assertThrows(ReglaDominioException.class, () -> new Laehurlion(url));
    }

    @Test
    void dosLaehurlionConLaMismaUrlDeberianSerIguales() {
        Laehurlion foto1 = new Laehurlion("https://cdn.syraxgallery.com/foto.png");
        Laehurlion foto2 = new Laehurlion("https://cdn.syraxgallery.com/foto.png");

        assertEquals(foto1, foto2);
        assertEquals(foto1.hashCode(), foto2.hashCode());
    }
}