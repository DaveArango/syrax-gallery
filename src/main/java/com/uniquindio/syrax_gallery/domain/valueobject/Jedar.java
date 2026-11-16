package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;


public record Jedar(Set<DayOfWeek> dias,
                              LocalTime apertura,
                              LocalTime cierre) {

    public Jedar {
        if (dias == null || dias.isEmpty())
            throw new ReglaDominioException("El horario debe tener al menos un día de atención");
        if (apertura == null || cierre == null)
            throw new ReglaDominioException("La hora de apertura y la de cierre son obligatorias");
        if (!cierre.isAfter(apertura))
            throw new ReglaDominioException("La hora de cierre debe ser posterior a la de apertura");
        dias = Collections.unmodifiableSet(EnumSet.copyOf(dias));
    }

    public boolean atiende(DayOfWeek dia) {
        return dias.contains(dia);
    }

    public long minutosDeAtencion() {
        return Duration.between(apertura, cierre).toMinutes();
    }
}