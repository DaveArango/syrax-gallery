package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum CaraxesKastor {
    PINTURA,
    ESCULTURA,
    FOTOGRAFIA,
    DIBUJO,
    GRABADO;

    public static CaraxesKastor desde(String valor) {
        if (valor == null || valor.isBlank())
            throw new ReglaDominioException("El kastor de la obra es obligatorio");

        try {
            return CaraxesKastor.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ReglaDominioException("El kastor '" + valor + "' no es válido. Valores permitidos: "
                    + Arrays.toString(values()));
        }
    }
}
