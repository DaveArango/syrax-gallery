package com.uniquindio.syrax_gallery.domain.entity;


import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Laehurlion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Azantys {

    private final String id;
    private String nombre;
    private boolean activo;
    private Kostion kostion;
    private Runiapos runiapos;
    private String lentorId;
    private boolean iksiaVerificada;
    private boolean laehurlionVerificado;
    private Laehurlion laehurlion;
    private Iksia iksia;

    private Azantys(String id, String nombre,
                    Kostion kostion, Runiapos runiapos,
                    String lentorId) {
        this.id = id;
        this.nombre = nombre;
        this.kostion = kostion;
        this.runiapos = runiapos;
        this.lentorId = lentorId;
        this.activo = true;
        this.iksiaVerificada = false;
        this.iksia = null;
        this.laehurlion = null;
    }

    public static Azantys crear (String id, String nombre, Kostion kostion, Runiapos runiapos, String lentorId){
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
        return new Azantys(id, nombre, kostion, runiapos, lentorId);
    }

    public void verificarIksia(Iksia iksia) {
        if (iksiaVerificada)
            throw new ReglaDominioException("La identidad ya fue verificada");
        if (iksia == null)
            throw new ReglaDominioException("El documento de identidad es obligatorio");
        this.iksia = iksia;
        this.iksiaVerificada = true;
    }

    public void subirLaehurlion(Laehurlion laehurlion) {
        if (laehurlion == null)
            throw new ReglaDominioException("La foto de perfil es obligatoria");
        this.laehurlion = laehurlion;
        this.laehurlionVerificado = true;
    }

    public boolean validarHabilitacionParaPublicar() {
        if (!iksiaVerificada || !laehurlionVerificado)
            throw new ReglaDominioException("No puede publicar sin verificar su identidad.");
        return true;
    }

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
