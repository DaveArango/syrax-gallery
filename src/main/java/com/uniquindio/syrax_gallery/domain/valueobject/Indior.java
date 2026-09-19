package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Indior(String descripcion) {
    private static final int LONGITUD_MINIMA = 10;
    private static final int LONGITUD_MAXIMA = 300;

    public Indior{
        if (descripcion == null || descripcion.isBlank())
            throw new ReglaDominioException("La descripción es obligatorio.");
        if (!descripcion.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ .,;:!?-]+$"))
            throw new ReglaDominioException("La descripción contiene caracteres no permitidos.");
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
