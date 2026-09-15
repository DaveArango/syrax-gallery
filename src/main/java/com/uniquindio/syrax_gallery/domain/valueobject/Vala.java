package com.uniquindio.syrax_gallery.domain.valueobject;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;


public record Vala(double monto, String divisa) {

    public Vala {
        if (monto < 0)
            throw new ReglaDominioException("Monto inválido: no puede ser negativo");
        if (divisa == null || divisa.isBlank())
            throw new ReglaDominioException("Divisa requerida");
        if (divisa.length() != 3)
            throw new ReglaDominioException("La divisa debe ser un código ISO de 3 letras");
    }

}
