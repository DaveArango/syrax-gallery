package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Sunfyre {

    private final String id;
    private final String idAzantys;
    private final String idZentys;
    private Sari sari;
    private SunfyreKastor kastor;
    private Alion alion;
    private final Vala valaCongelado;
    private DohaeroxJeda dohaeroxJeda;

    private Sunfyre(String id, String idAzantys, String idZentys, Sari sari,
                    SunfyreKastor kastor, Alion alion, Vala valaCongelado) {
        this.id = id;
        this.idAzantys = idAzantys;
        this.idZentys = idZentys;
        this.sari = sari;
        this.kastor = kastor;
        this.alion = alion;
        this.valaCongelado = valaCongelado;
        this.dohaeroxJeda = DohaeroxJeda.PENDIENTE;
    }

    public static Sunfyre solicitar(String id, String idAzantys, String idZentys, Sari sari,
                                    SunfyreKastor kastor, Alion alion, Vala valaCongelado) {
        if (id == null || id.isBlank())
            throw new ReglaDominioException("El id de la Sunfyre es obligatorio");
        if (idAzantys == null || idAzantys.isBlank())
            throw new ReglaDominioException("El idAzantys es obligatorio");
        if (idZentys == null || idZentys.isBlank())
            throw new ReglaDominioException("El idZentys es obligatorio");
        if (sari == null)
            throw new ReglaDominioException("El sari (ventana de tiempo) es obligatorio");
        if (kastor == null)
            throw new ReglaDominioException("El kastor (tipo de servicio) es obligatorio");
        if (alion == null)
            throw new ReglaDominioException("El alion (ubicación) es obligatorio");
        if (valaCongelado == null)
            throw new ReglaDominioException("El precio pactado es obligatorio");

        return new Sunfyre(id, idAzantys, idZentys, sari, kastor, alion, valaCongelado);
    }

    public void confirmar() {
        if (dohaeroxJeda != DohaeroxJeda.PENDIENTE)
            throw new ReglaDominioException("Solo una Sunfyre PENDIENTE puede confirmarse");
        this.dohaeroxJeda = DohaeroxJeda.ABONADO;
    }

    public void reprogramar(Sari nuevoSari) {
        if (dohaeroxJeda != DohaeroxJeda.ABONADO)
            throw new ReglaDominioException("Solo una Sunfyre ABONADA puede reprogramarse");
        if (nuevoSari == null)
            throw new ReglaDominioException("El nuevo sari es obligatorio");
        this.sari = nuevoSari;
    }

    public void iniciarEjecucion() {
        if (dohaeroxJeda != DohaeroxJeda.ABONADO)
            throw new ReglaDominioException("Solo una Sunfyre ABONADA puede iniciar ejecución");
        this.dohaeroxJeda = DohaeroxJeda.EN_EJECUCION;
    }

    public void finalizar() {
        if (dohaeroxJeda != DohaeroxJeda.EN_EJECUCION)
            throw new ReglaDominioException("Solo una Sunfyre en ejecución puede finalizar");
        this.dohaeroxJeda = DohaeroxJeda.COMPLETADO;
    }

    public void cancelar(String motivo) {
        if (dohaeroxJeda != DohaeroxJeda.PENDIENTE && dohaeroxJeda != DohaeroxJeda.ABONADO)
            throw new ReglaDominioException("Solo una Sunfyre PENDIENTE o ABONADA puede cancelarse");
        if (motivo == null || motivo.isBlank())
            throw new ReglaDominioException("El motivo de cancelación es obligatorio");
        this.dohaeroxJeda = DohaeroxJeda.CANCELADO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sunfyre that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
