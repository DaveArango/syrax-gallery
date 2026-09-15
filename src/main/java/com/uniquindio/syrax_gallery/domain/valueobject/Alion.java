package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

public record Alion(String ciudad,
                    String pais,
                    double latitud,
                    double longitud) {

    public Alion {
        if (ciudad == null || ciudad.isBlank())
            throw new ReglaDominioException("La ciudad es obligatoria");
        if (pais == null || pais.isBlank())
            throw new ReglaDominioException("El país es obligatorio");
        if (latitud < -90 || latitud > 90)
            throw new ReglaDominioException("Latitud fuera de rango");
        if (longitud < -180 || longitud > 180)
            throw new ReglaDominioException("Longitud fuera de rango");
    }
}
