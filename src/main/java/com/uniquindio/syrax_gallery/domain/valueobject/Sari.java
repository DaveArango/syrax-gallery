package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

import java.time.LocalDateTime;

public record Sari(LocalDateTime inicio, LocalDateTime fin) {

    public Sari {
        if (inicio == null || fin == null)
            throw new ReglaDominioException("Inicio y fin son obligatorios");
        if (!fin.isAfter(inicio))
            throw new ReglaDominioException("El fin debe ser posterior al inicio");
        if (inicio.isBefore(LocalDateTime.now()))
            throw new ReglaDominioException("No se permiten ventanas en el pasado");
    }

}
