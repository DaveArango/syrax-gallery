package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Runiapos(String direccion) {
    public Runiapos {
        if (direccion == null || direccion.isBlank())
            throw new ReglaDominioException("El email es obligatorio");
        if (!direccion.contains("@") || !direccion.contains("."))
            throw new ReglaDominioException("Email inválido");
        if (direccion.startsWith("@") || direccion.endsWith("@"))
            throw new ReglaDominioException("Email inválido");
    }
}
