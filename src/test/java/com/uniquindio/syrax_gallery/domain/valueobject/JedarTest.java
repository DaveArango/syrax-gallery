package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JedarTest {

    @Test
    void deberiaSaberQueDiasAtiendeYCuantosMinutos() {
        // Arrange
        Jedar horario = new Jedar(
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), LocalTime.of(9, 0), LocalTime.of(18, 0));

        // Act & Assert
        assertTrue(horario.atiende(DayOfWeek.MONDAY));
        assertFalse(horario.atiende(DayOfWeek.SUNDAY));
        assertEquals(540, horario.minutosDeAtencion());
    }

    @Test
    void noDeberiaCrearseSinDias() {
        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> new Jedar(Set.of(), LocalTime.of(9, 0), LocalTime.of(18, 0)));

        assertEquals("El horario debe tener al menos un día de atención", ex.getMessage());
    }

    @Test
    void elCierreDebeSerDespuesDeLaApertura() {
        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> new Jedar(EnumSet.of(DayOfWeek.MONDAY), LocalTime.of(18, 0), LocalTime.of(9, 0)));
    }
}
