package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Gelior;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Kelitis {

    private final String id;
    private final String idAzantys;
    private final String idZentys;
    private final Vala valaCongelado;
    private Gelior gelior;

    private Kelitis(String id, String idAzantys, String idZentys, Vala valaCongelado) {
        this.id = id;
        this.idAzantys = idAzantys;
        this.idZentys = idZentys;
        this.valaCongelado = valaCongelado;
        this.gelior = Gelior.RETENIDO;
    }

    public static Kelitis realizar(String id, String idAzantys, String idZentys, Vala valaCongelado) {
        if (id == null || id.isBlank())
            throw new ReglaDominioException("El id del Kelitis es obligatorio");
        if (idAzantys == null || idAzantys.isBlank())
            throw new ReglaDominioException("El idAzantys es obligatorio");
        if (idZentys == null || idZentys.isBlank())
            throw new ReglaDominioException("El idZentys es obligatorio");
        if (valaCongelado == null)
            throw new ReglaDominioException("El monto retenido es obligatorio");

        return new Kelitis(id, idAzantys, idZentys, valaCongelado);
    }

    public void liberarPago() {
        if (gelior != Gelior.RETENIDO)
            throw new ReglaDominioException("Solo un Kelitis RETENIDO puede liberar el pago");

        this.gelior = Gelior.LIBERADO;
    }

    public void completar() {
        if (gelior != Gelior.LIBERADO)
            throw new ReglaDominioException("Solo un Kelitis LIBERADO puede completarse");

        this.gelior = Gelior.COMPLETADO;
    }

    public void solicitarReembolso(String motivo) {
        if (gelior != Gelior.RETENIDO)
            throw new ReglaDominioException("Solo un Kelitis RETENIDO puede reembolsarse");
        if (motivo == null || motivo.isBlank())
            throw new ReglaDominioException("El motivo del reembolso es obligatorio");

        this.gelior = Gelior.REEMBOLSADO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Kelitis that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
