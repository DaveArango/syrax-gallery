package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SariTest {

    private final LocalDateTime base = LocalDateTime.now().plusDays(10).withHour(10).withMinute(0).withSecond(0).withNano(0);

    @Test
    void dosVentanasQueSeCruzanSeSolapan() {
        Sari cita = new Sari(base, base.plusHours(3));
        Sari otra = new Sari(base.plusHours(2), base.plusHours(4));

        assertTrue(cita.seSolapaCon(otra));
        assertTrue(otra.seSolapaCon(cita));
    }

    @Test
    void unaVentanaDentroDeOtraSeSolapa() {
        Sari cita = new Sari(base, base.plusHours(5));
        Sari dentro = new Sari(base.plusHours(1), base.plusHours(2));

        assertTrue(cita.seSolapaCon(dentro));
    }

    @Test
    void ventanasSeguidasNoSeSolapan() {
        Sari cita = new Sari(base, base.plusHours(2));
        Sari siguiente = new Sari(base.plusHours(2), base.plusHours(4));

        assertFalse(cita.seSolapaCon(siguiente));
    }

    @Test
    void ventanasSeparadasNoSeSolapan() {
        Sari cita = new Sari(base, base.plusHours(1));
        Sari otroDia = new Sari(base.plusDays(1), base.plusDays(1).plusHours(1));

        assertFalse(cita.seSolapaCon(otroDia));
    }

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
