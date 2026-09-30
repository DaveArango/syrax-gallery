package com.uniquindio.syrax_gallery.domain.valueobject;


import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Sete(String nombre) {

    public Sete {
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre de la obra es obligatorio");
        if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ ]+$")) {
            throw new ReglaDominioException("El nombre del Zentys no puede contener caracteres especiales.");
        }
        if (nombre.trim().length() < 3)
            throw new ReglaDominioException("El nombre de la obra debe tener al menos 3 caracteres");
        if (nombre.length() > 100)
            throw new ReglaDominioException("El nombre de la obra no puede superar 100 caracteres");
    }
}
