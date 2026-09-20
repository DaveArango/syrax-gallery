package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IksiaTest {
    @Test
    void crearIksiaValidaConNumeroEntre6Y10Digitos() {
        String numero = "1234567";

        Iksia iksia = new Iksia(numero);

        assertEquals(numero, iksia.numero());
    }

    @Test
    void numeroEsNulo() {
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> new Iksia(null));

        assertEquals("El número de documento es obligatorio", ex.getMessage());
    }

    @Test
    void numeroEstaEnBlanco() {
        String numero = "   ";

        assertThrows(ReglaDominioException.class, () -> new Iksia(numero));
    }

    @Test
    void tieneMenosDe6Digitos() {
        String numero = "12345";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> new Iksia(numero));

        assertEquals("El número de documento debe tener entre 6 y 10 dígitos", ex.getMessage());
    }

    @Test
    void tieneMasDe10Digitos() {
        String numero = "12345678901";

        assertThrows(ReglaDominioException.class, () -> new Iksia(numero));
    }

    @Test
    void identidadContieneLetrasUOtrosCaracteres() {
        String numeroConLetra = "12a4567";
        String numeroConGuion = "123-456";

        assertThrows(ReglaDominioException.class, () -> new Iksia(numeroConLetra));
        assertThrows(ReglaDominioException.class, () -> new Iksia(numeroConGuion));
    }

    @Test
    void dosIksiaConElMismoNumero() {
        Iksia iksia1 = new Iksia("1234567");
        Iksia iksia2 = new Iksia("1234567");

        assertEquals(iksia1, iksia2);
        assertEquals(iksia1.hashCode(), iksia2.hashCode());
    }
}