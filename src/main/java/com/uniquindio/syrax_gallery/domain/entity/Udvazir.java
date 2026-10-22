package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Udvazir {

    private final String id;
    private final String zentysId;
    private final String caraxesId;
    private final String texto;

    private Udvazir(String id,
                    String zentysId,
                    String caraxesId,
                    String texto) {
        this.id = id;
        this.zentysId = zentysId;
        this.caraxesId = caraxesId;
        this.texto = texto;
    }

    public static Udvazir crear(String zentysId,
                                String caraxesId,
                                String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaDominioException("El comentario no puede estar vacío");
        }

        String id = java.util.UUID.randomUUID().toString();
        return new Udvazir(id,
                zentysId,
                caraxesId,
                texto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Udvazir otro)) return false;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
