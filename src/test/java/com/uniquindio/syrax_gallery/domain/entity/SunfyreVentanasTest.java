package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SunfyreVentanasTest {

    // Tatuaje de 3 horas, de lunes a viernes de 09:00 a 18:00
    private Sunfyre tatuaje() {
        return Sunfyre.crear("sf-001", "az-001",
                new Sete("Tatuaje realista"),
                new Jorva("Tatuaje realista en blanco y negro, incluye el diseño."),
                new Urnebion("https://cdn.syrax.com/servicios/sf-001.jpg"),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(300000, "COP"),
                180,
                new Jedar(EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), LocalTime.of(9, 0), LocalTime.of(18, 0)));
    }

    private LocalDate proximo(DayOfWeek dia) {
        return LocalDate.now().with(TemporalAdjusters.next(dia));
    }

    @Test
    void unDiaDeAtencionSeParteEnVentanasDelTamanoDelServicio() {
        // Arrange
        LocalDate lunes = proximo(DayOfWeek.MONDAY);

        // Act
        List<Sari> ventanas = tatuaje().ventanasDel(lunes);

        // Assert: 9 horas de atención y 3 de servicio dan 09-12, 12-15 y 15-18
        assertEquals(3, ventanas.size());
        assertEquals(new Sari(lunes.atTime(9, 0), lunes.atTime(12, 0)), ventanas.get(0));
        assertEquals(new Sari(lunes.atTime(15, 0), lunes.atTime(18, 0)), ventanas.get(2));
    }

    @Test
    void unDiaQueNoSeAtiendeNoTieneVentanas() {
        // Act
        List<Sari> ventanas = tatuaje().ventanasDel(proximo(DayOfWeek.SUNDAY));

        // Assert
        assertTrue(ventanas.isEmpty());
    }

    @Test
    void noSeOfreceUnaVentanaQueSePaseDeLaHoraDeCierre() {
        // Arrange: 2 horas de servicio en un horario de 09:00 a 14:00 (sobran 60 minutos)
        Sunfyre retrato = Sunfyre.crear("sf-003", "az-001",
                new Sete("Retrato a lapiz"),
                new Jorva("Retrato a lapiz hecho en vivo, se entrega enmarcado."),
                new Urnebion("https://cdn.syrax.com/servicios/sf-003.jpg"),
                SunfyreKastor.RETRATO_TRADICIONAL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(180000, "COP"),
                120,
                new Jedar(EnumSet.of(DayOfWeek.SATURDAY), LocalTime.of(9, 0), LocalTime.of(14, 0)));

        // Act
        List<Sari> ventanas = retrato.ventanasDel(proximo(DayOfWeek.SATURDAY));

        // Assert: caben 09-11 y 11-13; la de 13-15 se pasaría del cierre
        assertEquals(2, ventanas.size());
        assertEquals(LocalTime.of(13, 0), ventanas.get(1).fin().toLocalTime());
    }
}
