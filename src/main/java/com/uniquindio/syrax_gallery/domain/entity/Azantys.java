package com.uniquindio.syrax_gallery.domain.entity;


import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;

import java.util.Objects;

public class Azantys {

    private final String id;
    private String nombre;
    private boolean activo;
    private Kostion kostion;
    private Runiapos runiapos;
    private String lentorId;

    public Azantys(String id, String nombre, Kostion kostion, Runiapos runiapos, String lentorId) {
        if (id == null || id.isBlank())
            throw new ReglaDominioException("El id del Azantys es obligatorio");
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre del Azantys es obligatorio");
        if (kostion == null)
            throw new ReglaDominioException("La especialidad del Azantys es obligatorio");
        if (runiapos == null)
            throw new ReglaDominioException("El email del Azantys es obligatorio");
        if (lentorId != null && lentorId.isBlank())
            throw new ReglaDominioException("El lentorId no puede ser una cadena vacía");

        this.id = id;
        this.nombre = nombre;
        this.kostion = kostion;
        this.runiapos = runiapos;
        this.lentorId = lentorId;
        this.activo = true;
    }

    public void actualizarKostion(Kostion nuevoKostion) {
        if (nuevoKostion == null)
            throw new ReglaDominioException("La especialidad es obligatoria");
        this.kostion = nuevoKostion;
    }

    public Kostion getKostion() { return kostion; }

    public String getId() { return id; }

    public String getNombre() { return nombre; }

    public boolean isActivo() { return activo; }

    public Runiapos getRuniapos() { return runiapos; }
    
    public String getLentorId() { return lentorId; }

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
