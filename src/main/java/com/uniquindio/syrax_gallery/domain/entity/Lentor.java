package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;

import java.util.Objects;

public class Lentor {

    private final String id;
    private String nombre;
    private Alion ubicacion;
    private boolean activo;

    public Lentor(String id, String nombre, Alion ubicacion) {
        if (id == null)
            throw new ReglaDominioException("El id del Lentor es obligatorio");
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre del Lentor es obligatorio");
        if (ubicacion == null)
            throw new ReglaDominioException("La ubicación del Lentor es obligatoria");
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.activo = true;
    }

    public void desactivar() {
        if (!activo)
            throw new ReglaDominioException("El Lentor ya está desactivado");
        this.activo = false;
    }

    public void reactivar() {
        if (activo)
            throw new ReglaDominioException("El Lentor ya está activo");
        this.activo = true;
    }

    public void cambiarUbicacion(Alion nuevaUbicacion) {
        if (nuevaUbicacion == null)
            throw new ReglaDominioException("La ubicación es obligatoria");
        this.ubicacion = nuevaUbicacion;
    }

    public String getId() { return id; }

    public String getNombre() { return nombre; }

    public Alion getUbicacion() { return ubicacion; }
    
    public boolean isActivo() { return activo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lentor that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}