package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SariTest {

    @Test
    void noDebeCrearSariSinInicioOFin() {
        assertThrows(ReglaDominioException.class, () -> new Sari(null, LocalDateTime.now().plusDays(1)));
        assertThrows(ReglaDominioException.class, () -> new Sari(LocalDateTime.now().plusDays(1), null));
    }

    @Test
    void noDebeCrearSariConFinAntesDelInicio() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(2);

        assertThrows(ReglaDominioException.class, () -> new Sari(inicio, inicio.minusHours(1)));
    }

    @Test
    void iniciaEnElPasadoDetectaVentanasPasadas() {
        Sari pasado = new Sari(LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1));
        Sari futuro = new Sari(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1));

        assertTrue(pasado.iniciaEnElPasado());
        assertFalse(futuro.iniciaEnElPasado());
    }
}
