package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.SindesJeda;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Raíz del agregado Sindes: la compra de un Caraxes por parte de un Zentys.
 *
 * Invariantes:
 *  - todo Sindes nace PENDIENTE;
 *  - el Vala queda congelado al momento de comprar (no cambia si la obra cambia de precio);
 *  - solo un Sindes PENDIENTE puede confirmarse o cancelarse;
 *  - solo el Azantys de la obra confirma y solo el Zentys que compró cancela.
 */
@Getter
public class Sindes {

    private final String id;
    private final String idCaraxes;   // solo el id: Caraxes es OTRO agregado
    private final String idAzantys;   // el vendedor, copiado de la obra al comprar
    private final String idZentys;    // el comprador
    private final Vala valaCongelado;
    private final LocalDateTime fecha;
    private SindesJeda sindesJeda;

    private Sindes(String id, String idCaraxes, String idAzantys, String idZentys,
                   Vala valaCongelado, LocalDateTime fecha, SindesJeda sindesJeda) {
        this.id = id;
        this.idCaraxes = idCaraxes;
        this.idAzantys = idAzantys;
        this.idZentys = idZentys;
        this.valaCongelado = valaCongelado;
        this.fecha = fecha;
        this.sindesJeda = sindesJeda;
    }

    public static Sindes realizar(String id, String idCaraxes, String idAzantys, String idZentys,
                                  Vala valaDeLaObra) {
        if (id == null || id.isBlank())
            throw new ReglaDominioException("El id del Sindes es obligatorio");
        if (idCaraxes == null || idCaraxes.isBlank())
            throw new ReglaDominioException("El Sindes debe indicar el Caraxes que se compra");
        if (idAzantys == null || idAzantys.isBlank())
            throw new ReglaDominioException("El Sindes debe indicar el Azantys que vende");
        if (idZentys == null || idZentys.isBlank())
            throw new ReglaDominioException("El Sindes debe indicar el Zentys que compra");
        if (valaDeLaObra == null || valaDeLaObra.monto() <= 0)
            throw new ReglaDominioException("El Sindes debe tener un Vala mayor a cero");

        return new Sindes(id, idCaraxes, idAzantys, idZentys, valaDeLaObra,
                LocalDateTime.now(), SindesJeda.PENDIENTE);
    }

    /** Solo para el adaptador de persistencia: revive un Sindes que ya existía, sin validar. */
    public static Sindes reconstruir(String id, String idCaraxes, String idAzantys, String idZentys,
                                     Vala valaCongelado, LocalDateTime fecha, SindesJeda sindesJeda) {
        return new Sindes(id, idCaraxes, idAzantys, idZentys, valaCongelado, fecha, sindesJeda);
    }

    // ---------- Comportamiento ----------

    public void confirmar() {
        verificarTransicion(SindesJeda.COMPLETADA, "Solo un Sindes PENDIENTE puede confirmarse");
        this.sindesJeda = SindesJeda.COMPLETADA;
    }

    public void cancelar() {
        verificarTransicion(SindesJeda.CANCELADA, "Solo un Sindes PENDIENTE puede cancelarse");
        this.sindesJeda = SindesJeda.CANCELADA;
    }

    public void validarAzantys(String idAzantys) {
        if (idAzantys == null || !this.idAzantys.equals(idAzantys))
            throw new ReglaDominioException("Solo el Azantys de la obra puede confirmar este Sindes");
    }

    public void validarZentys(String idZentys) {
        if (idZentys == null || !this.idZentys.equals(idZentys))
            throw new ReglaDominioException("Solo el Zentys que hizo el Sindes puede cancelarlo");
    }

    private void verificarTransicion(SindesJeda siguiente, String mensaje) {
        if (!this.sindesJeda.puedeTransicionarA(siguiente))
            throw new ReglaDominioException(mensaje);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sindes otro)) return false;
        return id.equals(otro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
