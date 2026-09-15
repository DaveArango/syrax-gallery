package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

import java.util.Objects;

public class Zentys {
    private final String id;
    private String nombre;
    private String email;

    public Zentys(String id,
                  String nombre,
                  String email) {
        if (id == null)
            throw new ReglaDominioException("El id del Zentys es obligatorio");
        if (email == null || !email.contains("@"))
            throw new ReglaDominioException("Email inválido");
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public String getId() { return id; }

    public String getNombre() { return nombre; }

    public String getEmail() { return email; }

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
