package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DohaeragonReservaTest {

    private Sari enCincoDias() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(5);
        return new Sari(inicio, inicio.plusHours(3));
    }

    @Test
    void laReservaNacePendienteYLigadaASuSunfyre() {
        // Act
        Dohaeragon dohaeragon = Dohaeragon.reservar("dh-100", "sf-001", "az-001", "zentys-01", enCincoDias(),
                SunfyreKastor.ARTE_PIEL, new Alion("Armenia", "Colombia", 4.5339, -75.6811), new Vala(300000, "COP"));

        // Assert
        assertEquals("sf-001", dohaeragon.getIdSunfyre());
        assertEquals(DohaeroxJeda.PENDIENTE, dohaeragon.getDohaeroxJeda());
        assertEquals(new Vala(300000, "COP"), dohaeragon.getValaCongelado());
    }

    @Test
    void noSePuedeReservarSinIndicarLaSunfyre() {
        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Dohaeragon.reservar("dh-100", " ", "az-001", "zentys-01", enCincoDias(),
                        SunfyreKastor.ARTE_PIEL, new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                        new Vala(300000, "COP")));

        assertEquals("La Dohaeragon debe indicar la Sunfyre que se reserva", ex.getMessage());
    }

    @Test
    void unaCitaCreadaPorElAzantysNoTieneSunfyre() {
        // Act
        Dohaeragon dohaeragon = Dohaeragon.solicitar("dh-101", "az-001", "zentys-01", enCincoDias(),
                SunfyreKastor.ARTE_PIEL, new Alion("Armenia", "Colombia", 4.5339, -75.6811), new Vala(300000, "COP"));

        // Assert
        assertNull(dohaeragon.getIdSunfyre());
    }
}
