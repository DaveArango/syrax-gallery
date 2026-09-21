package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SunfyreTest {
    private Sunfyre sunfyrePendiente() {
        Sari sari = new Sari(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2));
        Vala precio = new Vala(150000, "COP");
        Alion alion = new Alion("Armenia", "Colombia", 4.5339, -75.6811);
        return Sunfyre.solicitar("sf-001", "az-001", "ze-001", sari, SunfyreKastor.ARTE_PIEL, alion, precio);
    }

    @Test
    void noPermitirReprogramarUnaSunfyrePendiente() {
        Sunfyre sunfyre = sunfyrePendiente();
        Sari nuevoSari = new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2));

        assertThrows(ReglaDominioException.class, () -> sunfyre.reprogramar(nuevoSari));

        assertEquals(EstadoSunfyre.PENDIENTE, sunfyre.getEstado());
    }

    @Test
    void noPermitirCancelarUnaSunfyreYaCompletada() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.confirmar();
        sunfyre.iniciarEjecucion();
        sunfyre.finalizar();

        assertThrows(ReglaDominioException.class, () -> sunfyre.cancelar("Ya no la quiero"));

        assertEquals(EstadoSunfyre.COMPLETADO, sunfyre.getEstado()); // sigue completada, no se canceló
    }
}
