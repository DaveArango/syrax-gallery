package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Iksia(String numero) {

    public Iksia {
        if (numero == null || numero.isBlank())
            throw new ReglaDominioException("El número de documento es obligatorio");
        if (!numero.matches("\\d{6,10}"))
            throw new ReglaDominioException("El número de documento debe tener entre 6 y 10 dígitos");
    }
}