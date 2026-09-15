package com.uniquindio.syrax_gallery.domain.entity;


import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;

import java.util.Objects;

public class Azantys {
    private final String id;
    private String nombre;
    private String especialidad;
    private boolean activo;

    public Azantys(String id, String nombre, String especialidad) {
        if (id == null)
            throw new ReglaDominioException("El id del Azantys es obligatorio");
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre del Azantys es obligatorio");
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = true;
    }

    public String getId() { return id; }

    public String getNombre() { return nombre; }

    public String getEspecialidad() { return especialidad; }

    public boolean isActivo() { return activo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Azantys that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
