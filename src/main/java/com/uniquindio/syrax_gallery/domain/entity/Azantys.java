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
    private Iksia iksia;
    private boolean iksiaVerificada;
    private Laehurlion laehurlion;
    private boolean laehurlionVerificado;
    private boolean identidadAprobada;

    private Azantys(String id, String nombre, boolean activo,
                    Kostion kostion, Runiapos runiapos,
                    Iksia iksia, boolean iksiaVerificada,
                    Laehurlion laehurlion, boolean laehurlionVerificado,
                    boolean identidadAprobada) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
        this.kostion = kostion;
        this.runiapos = runiapos;
        this.iksia = iksia;
        this.iksiaVerificada = iksiaVerificada;
        this.laehurlion = laehurlion;
        this.laehurlionVerificado = laehurlionVerificado;
        this.identidadAprobada = identidadAprobada;
    }

    private Azantys(String id, String nombre, Kostion kostion, Runiapos runiapos) {
        this(id, nombre, true, kostion, runiapos, null, false, null, false, false);
    }

    public static Azantys crear(String id, String nombre, Kostion kostion, Runiapos runiapos) {
        if (id == null || id.isBlank())
            throw new ReglaDominioException("El id del Azantys es obligatorio");
        if (nombre == null || nombre.isBlank())
            throw new ReglaDominioException("El nombre del Azantys es obligatorio");
        if (kostion == null)
            throw new ReglaDominioException("La especialidad del Azantys es obligatoria");
        if (runiapos == null)
            throw new ReglaDominioException("El correo del Azantys es obligatorio");
        return new Azantys(id, nombre, kostion, runiapos);
    }

    public static Azantys reconstruir(String id, String nombre, boolean activo,
                                      Kostion kostion, Runiapos runiapos,
                                      Iksia iksia, boolean iksiaVerificada,
                                      Laehurlion laehurlion, boolean laehurlionVerificado,
                                      boolean identidadAprobada) {
        return new Azantys(id, nombre, activo, kostion, runiapos,
                iksia, iksiaVerificada, laehurlion, laehurlionVerificado, identidadAprobada);
    }

    public void validarTitular(String idSolicitante) {
        if (idSolicitante == null || !this.id.equals(idSolicitante))
            throw new ReglaDominioException("Solo el propio Azantys puede cargar su identidad");
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

    public void aprobarIdentidad() {
        validarDocumentosCargadosSinRevisar();
        this.identidadAprobada = true;
    }

    public void rechazarIdentidad(String motivo) {
        validarDocumentosCargadosSinRevisar();
        if (motivo == null || motivo.isBlank())
            throw new ReglaDominioException("El motivo del rechazo es obligatorio");
        this.iksia = null;
        this.iksiaVerificada = false;
    }

    private void validarDocumentosCargadosSinRevisar() {
        if (!iksiaVerificada || !laehurlionVerificado)
            throw new ReglaDominioException("El Azantys aún no ha cargado su identidad y su foto de perfil");
        if (identidadAprobada)
            throw new ReglaDominioException("La identidad ya fue aprobada");
    }

    public boolean estaPendienteDeRevision() {
        return iksiaVerificada && laehurlionVerificado && !identidadAprobada;
    }

    /** RN5: documentos cargados Y aprobados por el Administrador. No lanza excepción. */
    public boolean estaHabilitadoParaPublicar() {
        return iksiaVerificada && laehurlionVerificado && identidadAprobada;
    }

    public boolean validarHabilitacionParaPublicar() {
        if (!estaHabilitadoParaPublicar())
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