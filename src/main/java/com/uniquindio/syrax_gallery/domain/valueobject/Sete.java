package com.uniquindio.syrax_gallery.domain.valueobject;


import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Sete(String nombre) {

    public Sete {
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre de la obra física es obligatorio");
        if (nombre.trim().length() < 3)
            throw new ReglaDominioException("El nombre de la obra física debe tener al menos 3 caracteres");
        if (nombre.length() > 100)
            throw new ReglaDominioException("El nombre de la obra física no puede superar 100 caracteres");
    }
}
