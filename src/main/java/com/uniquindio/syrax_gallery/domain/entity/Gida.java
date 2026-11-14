package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Gida: el comentario que un Zentys deja sobre un Caraxes que compró.
 * Que de verdad lo haya comprado lo revisa VerificadorDeSindes, porque eso depende de otro agregado.
 */
@Getter
public class Gida {

    private static final int LONGITUD_MAXIMA = 500;

    private final String id;
    private final String idZentys;
    private final String idCaraxes;
    private final String texto;
    private final LocalDateTime fecha;

    private Gida(String id, String idZentys, String idCaraxes, String texto, LocalDateTime fecha) {
        this.id = id;
        this.idZentys = idZentys;
        this.idCaraxes = idCaraxes;
        this.texto = texto;
        this.fecha = fecha;
    }

    public static Gida crear(String id, String idZentys, String idCaraxes, String texto) {
        if (id == null || id.isBlank())
            throw new ReglaDominioException("El id del Gida es obligatorio");
        if (idZentys == null || idZentys.isBlank())
            throw new ReglaDominioException("El Gida debe indicar el Zentys que comenta");
        if (idCaraxes == null || idCaraxes.isBlank())
            throw new ReglaDominioException("El Gida debe indicar el Caraxes que se comenta");
        if (texto == null || texto.isBlank())
            throw new ReglaDominioException("El Gida no puede estar vacío");
        if (texto.length() > LONGITUD_MAXIMA)
            throw new ReglaDominioException("El Gida no puede superar " + LONGITUD_MAXIMA + " caracteres");

        return new Gida(id, idZentys, idCaraxes, texto.trim(), LocalDateTime.now());
    }

    /** Solo para el adaptador de persistencia. */
    public static Gida reconstruir(String id, String idZentys, String idCaraxes, String texto,
                                   LocalDateTime fecha) {
        return new Gida(id, idZentys, idCaraxes, texto, fecha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gida otro)) return false;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
