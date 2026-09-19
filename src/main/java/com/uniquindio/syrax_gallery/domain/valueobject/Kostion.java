package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Kostion(String descripcion) {

    private static final int LONGITUD_MINIMA = 3;
    private static final int LONGITUD_MAXIMA = 100;

    public Kostion {
        if (descripcion == null || descripcion.isBlank())
            throw new ReglaDominioException("La especialidad no puede estar vacía");
        if (descripcion.length() < LONGITUD_MINIMA)
            throw new ReglaDominioException(
                    "La especialidad debe tener al menos " + LONGITUD_MINIMA + " caracteres");
        if (descripcion.length() > LONGITUD_MAXIMA)
            throw new ReglaDominioException(
                    "La especialidad no puede superar " + LONGITUD_MAXIMA + " caracteres");
    }

    public boolean coincideCon(String otraDescripcion) {
        if (otraDescripcion == null) return false;
        return this.descripcion.trim().equalsIgnoreCase(otraDescripcion.trim());
    }
}
