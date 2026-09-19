package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Indior;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Zentys {
    private final String id;
    private String nombre;
    private Runiapos runiapos;
    private Indior indior;
    private boolean activo;

    public Zentys(String id,
                  String nombre,
                  Runiapos runiapos,
                  Indior indior) {
        this.id = id;
        this.nombre = nombre;
        this.runiapos = runiapos;
        this.indior = indior;
        this.activo = true;
    }

    public static Zentys crear(String id,
                               String nombre,
                               Runiapos runiapos,
                               Indior indior){
        if (id == null)
            throw new ReglaDominioException("El id del Zentys es obligatorio");
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre del Zentys es obligatorio.");
        if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ ]+$")) {
            throw new ReglaDominioException("El nombre del Zentys no puede contener caracteres especiales.");
        }
        return new Zentys(id, nombre, runiapos, indior);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zentys that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
