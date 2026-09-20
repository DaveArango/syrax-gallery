package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Laehurlion(String url) {

    public Laehurlion {
        if (url == null || url.isBlank())
            throw new ReglaDominioException("La foto de perfil es obligatoria");
    }
}
