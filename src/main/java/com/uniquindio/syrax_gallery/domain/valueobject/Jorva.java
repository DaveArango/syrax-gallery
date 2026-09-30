package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Jorva(String descripcion) {

    public Jorva {
        if (descripcion == null || descripcion.isBlank())
            throw new ReglaDominioException("La descripción de la obra es obligatoria");
        if (descripcion.trim().length() < 10)
            throw new ReglaDominioException("La descripción de la obra debe tener al menos 10 caracteres");
        if (descripcion.length() > 1000)
            throw new ReglaDominioException("La descripción de la obra no puede superar 1000 caracteres");
    }
}
