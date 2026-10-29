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
    private Kanez kanez;

    private Sunfyre(String id, String idAzantys, String idZentys, Sari sari,
                    SunfyreKastor kastor, Alion alion, Vala valaCongelado,
                    DohaeroxJeda dohaeroxJeda, Kanez kanez) {
        this.id = id;
        this.idAzantys = idAzantys;
        this.idZentys = idZentys;
        this.sari = sari;
        this.kastor = kastor;
        this.alion = alion;
        this.valaCongelado = valaCongelado;
        this.dohaeroxJeda = dohaeroxJeda;
        this.kanez = kanez;
    }

    private Sunfyre(String id, String idAzantys, String idZentys, Sari sari,
                    SunfyreKastor kastor, Alion alion, Vala valaCongelado) {
        this(id, idAzantys, idZentys, sari, kastor, alion, valaCongelado,
                DohaeroxJeda.PENDIENTE, Kanez.BORRADOR);
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
        if (sari.iniciaEnElPasado())
            throw new ReglaDominioException("No se permiten ventanas en el pasado");
        if (kastor == null)
            throw new ReglaDominioException("El kastor (tipo de servicio) es obligatorio");
        if (alion == null)
            throw new ReglaDominioException("El alion (ubicación) es obligatorio");
        if (valaCongelado == null)
            throw new ReglaDominioException("El precio pactado es obligatorio");

        return new Sunfyre(id, idAzantys, idZentys, sari, kastor, alion, valaCongelado);
    }

    public static Sunfyre reconstruir(String id, String idAzantys, String idZentys, Sari sari,
                                      SunfyreKastor kastor, Alion alion, Vala valaCongelado,
                                      DohaeroxJeda dohaeroxJeda, Kanez kanez) {
        return new Sunfyre(id, idAzantys, idZentys, sari, kastor, alion, valaCongelado,
                dohaeroxJeda, kanez);
    }

    public void validarAutoria(String idAzantys) {
        if (idAzantys == null || !this.idAzantys.equals(idAzantys))
            throw new ReglaDominioException("Solo el Azantys creador puede gestionar esta Sunfyre");
    }

    public void validarParticipante(String idParticipante) {
        if (idParticipante == null
                || (!idParticipante.equals(this.idAzantys) && !idParticipante.equals(this.idZentys)))
            throw new ReglaDominioException("Solo el Azantys o el Zentys de la Sunfyre pueden realizar esta acción");
    }

    public void validarCliente(String idZentys) {
        if (idZentys == null || !this.idZentys.equals(idZentys))
            throw new ReglaDominioException("Solo el Zentys de la Sunfyre puede constituir su Kelitis");
    }

    public void publicar(Kanez kanez) {
        if (kanez == null)
            throw new ReglaDominioException("El destino de publicación es obligatorio");
        if (dohaeroxJeda != DohaeroxJeda.PENDIENTE)
            throw new ReglaDominioException("Solo una Sunfyre PENDIENTE puede publicarse");
        switch (kanez) {
            case EXHIBICION -> publicarParaExhibicion();
            case EN_VENTA -> publicarParaVenta();
            default -> throw new ReglaDominioException("Solo se puede publicar para EXHIBICION o EN_VENTA");
        }
    }

    private void publicarParaExhibicion() {
        if (kanez != Kanez.BORRADOR)
            throw new ReglaDominioException("Solo una Sunfyre en BORRADOR puede publicarse para exhibición");
        this.kanez = Kanez.EXHIBICION;
    }

    private void publicarParaVenta() {
        if (kanez != Kanez.BORRADOR && kanez != Kanez.EXHIBICION)
            throw new ReglaDominioException("Solo una Sunfyre en BORRADOR o EXHIBICION puede ponerse en venta");
        if (valaCongelado.monto() <= 0)
            throw new ReglaDominioException("Una Sunfyre debe tener un precio mayor a cero para ponerse en venta");
        this.kanez = Kanez.EN_VENTA;
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
        if (nuevoSari.iniciaEnElPasado())
            throw new ReglaDominioException("No se permiten ventanas en el pasado");
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