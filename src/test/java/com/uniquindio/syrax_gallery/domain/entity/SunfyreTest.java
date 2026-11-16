package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class SunfyreTest {

    private Jedar deNueveASeis() {
        return new Jedar(EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                LocalTime.of(9, 0), LocalTime.of(18, 0));
    }

    private Sunfyre tatuaje(double precio, int duracionMinutos, Jedar horario) {
        return Sunfyre.crear("sf-001", "az-001",
                new Sete("Tatuaje realista"),
                new Jorva("Tatuaje realista en blanco y negro, incluye el diseño."),
                new Urnebion("https://cdn.syrax.com/servicios/sf-001.jpg"),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(precio, "COP"),
                duracionMinutos,
                horario);
    }

    @Test
    void deberiaNacerActivoConSusDatos() {
        // Act
        Sunfyre sunfyre = tatuaje(300000, 180, deNueveASeis());

        // Assert
        assertTrue(sunfyre.isActivo());
        assertEquals("az-001", sunfyre.getIdAzantys());
        assertEquals(180, sunfyre.getDuracionMinutos());
        assertEquals(new Vala(300000, "COP"), sunfyre.getVala());
        assertDoesNotThrow(sunfyre::validarDisponible);
    }

    @Test
    void noDeberiaCrearseConPrecioEnCero() {
        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> tatuaje(0, 180, deNueveASeis()));

        assertEquals("La Sunfyre debe tener un precio mayor a cero", ex.getMessage());
    }

    @Test
    void noDeberiaDurarMenosDeMediaHora() {
        // Act & Assert
        assertThrows(ReglaDominioException.class, () -> tatuaje(300000, 20, deNueveASeis()));
    }

    @Test
    void laDuracionDebeCaberEnElHorarioDeAtencion() {
        // Arrange: atiende solo 2 horas, pero el servicio dura 3
        Jedar dosHoras = new Jedar(EnumSet.of(DayOfWeek.SATURDAY),
                LocalTime.of(9, 0), LocalTime.of(11, 0));

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> tatuaje(300000, 180, dosHoras));

        assertEquals("La duración del servicio no cabe en el horario de atención", ex.getMessage());
    }

    @Test
    void unaSunfyreInactivaNoEstaDisponible() {
        // Arrange
        Sunfyre activo = tatuaje(300000, 180, deNueveASeis());
        Sunfyre inactivo = Sunfyre.reconstruir(activo.getId(), activo.getIdAzantys(), activo.getSete(),
                activo.getJorva(), activo.getUrnebion(), activo.getKastor(), activo.getAlion(), activo.getVala(),
                activo.getDuracionMinutos(), activo.getJedar(), false);

        // Act & Assert
        assertThrows(ReglaDominioException.class, inactivo::validarDisponible);
    }
}
