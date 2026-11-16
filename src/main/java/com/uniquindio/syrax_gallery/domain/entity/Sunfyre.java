package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Jedar;
import com.uniquindio.syrax_gallery.domain.valueobject.Jorva;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.Sete;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Urnebion;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Sunfyre: el servicio que un Azantys ofrece en el catálogo
 * (por ejemplo "Tatuaje realista, 3 horas, $300.000, en Armenia").
 *
 * No es una cita: la cita es la Dohaeragon que un Zentys reserva sobre esta Sunfyre.
 *
 * Invariantes:
 *  - siempre tiene un Azantys dueño, nombre, descripción, imagen, tipo, ubicación y horario;
 *  - el precio es mayor a cero;
 *  - la duración va de 30 minutos a 8 horas y cabe dentro del horario de atención;
 *  - nace activa (visible en el catálogo).
 */
@Getter
public class Sunfyre {

    private static final int DURACION_MINIMA = 30;
    private static final int DURACION_MAXIMA = 480;

    private final String id;
    private final String idAzantys;
    private final Sete sete;               // nombre del servicio
    private final Jorva jorva;             // descripción
    private final Urnebion urnebion;       // imagen de referencia
    private final SunfyreKastor kastor;    // tipo de servicio
    private final Alion alion;             // dónde se presta
    private final Vala vala;               // precio
    private final int duracionMinutos;
    private final Jedar jedar;
    private final boolean activo;

    private Sunfyre(String id, String idAzantys, Sete sete, Jorva jorva, Urnebion urnebion,
                       SunfyreKastor kastor, Alion alion, Vala vala, int duracionMinutos,
                       Jedar jedar, boolean activo) {
        this.id = id;
        this.idAzantys = idAzantys;
        this.sete = sete;
        this.jorva = jorva;
        this.urnebion = urnebion;
        this.kastor = kastor;
        this.alion = alion;
        this.vala = vala;
        this.duracionMinutos = duracionMinutos;
        this.jedar = jedar;
        this.activo = activo;
    }

    public static Sunfyre crear(String id, String idAzantys, Sete sete, Jorva jorva, Urnebion urnebion,
                                   SunfyreKastor kastor, Alion alion, Vala vala, int duracionMinutos,
                                   Jedar jedar) {
        if (id == null || id.isBlank())
            throw new ReglaDominioException("El id de la Sunfyre es obligatorio");
        if (idAzantys == null || idAzantys.isBlank())
            throw new ReglaDominioException("La Sunfyre debe indicar el Azantys que la ofrece");
        if (sete == null)
            throw new ReglaDominioException("El nombre de la Sunfyre es obligatorio");
        if (jorva == null)
            throw new ReglaDominioException("La descripción de la Sunfyre es obligatoria");
        if (urnebion == null)
            throw new ReglaDominioException("La imagen de referencia de la Sunfyre es obligatoria");
        if (kastor == null)
            throw new ReglaDominioException("El kastor (tipo de servicio) es obligatorio");
        if (alion == null)
            throw new ReglaDominioException("El alion (ubicación) es obligatorio");
        if (vala == null || vala.monto() <= 0)
            throw new ReglaDominioException("La Sunfyre debe tener un precio mayor a cero");
        if (jedar == null)
            throw new ReglaDominioException("El horario de atención es obligatorio");
        if (duracionMinutos < DURACION_MINIMA || duracionMinutos > DURACION_MAXIMA)
            throw new ReglaDominioException("La duración debe estar entre " + DURACION_MINIMA
                    + " y " + DURACION_MAXIMA + " minutos");
        if (duracionMinutos > jedar.minutosDeAtencion())
            throw new ReglaDominioException("La duración del servicio no cabe en el horario de atención");

        return new Sunfyre(id, idAzantys, sete, jorva, urnebion, kastor, alion, vala,
                duracionMinutos, jedar, true);
    }

    /** Solo para el adaptador de persistencia: revive una Sunfyre que ya existía, sin validar. */
    public static Sunfyre reconstruir(String id, String idAzantys, Sete sete, Jorva jorva, Urnebion urnebion,
                                         SunfyreKastor kastor, Alion alion, Vala vala, int duracionMinutos,
                                         Jedar jedar, boolean activo) {
        return new Sunfyre(id, idAzantys, sete, jorva, urnebion, kastor, alion, vala,
                duracionMinutos, jedar, activo);
    }

    /**
     * Las ventanas (Sari) en que se puede prestar el servicio ese día, una detrás de otra:
     * de 09:00 a 18:00 con 3 horas de duración da 09-12, 12-15 y 15-18.
     * Si ese día no se atiende, no hay ninguna.
     */
    public List<Sari> ventanasDel(LocalDate fecha) {
        List<Sari> ventanas = new ArrayList<>();
        if (fecha == null || !jedar.atiende(fecha.getDayOfWeek())) {
            return ventanas;
        }
        LocalDateTime inicio = fecha.atTime(jedar.apertura());
        LocalDateTime cierre = fecha.atTime(jedar.cierre());
        while (!inicio.plusMinutes(duracionMinutos).isAfter(cierre)) {
            ventanas.add(new Sari(inicio, inicio.plusMinutes(duracionMinutos)));
            inicio = inicio.plusMinutes(duracionMinutos);
        }
        return ventanas;
    }

    public void validarDisponible() {
        if (!activo)
            throw new ReglaDominioException("Esta Sunfyre ya no está disponible");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sunfyre otro)) return false;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
