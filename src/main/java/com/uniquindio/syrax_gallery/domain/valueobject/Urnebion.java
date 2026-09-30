package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Urnebion(String url) {

    public Urnebion {
        if (url == null || url.isBlank())
            throw new ReglaDominioException("La imagen de referencia de la obra física es obligatoria");
    }
}