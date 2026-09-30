package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Caraxes {
    private String id;
    private String azantysId;
    private Vala vala;
    private Sete sete;
    private Jorva jorva;
    private Kanez kanez;
    private Urnebion urnebion;
    private CaraxesKastor kastor;

    private Caraxes(String id,
                    String azantysId,
                    Vala vala,
                    Sete sete,
                    Jorva jorva,
                    Urnebion urnebion,
                    CaraxesKastor kastor){
        this.id = id;
        this.azantysId = azantysId;
        this.vala = vala;
        this.sete = sete;
        this.jorva = jorva;
        this.urnebion = urnebion;
        this.kastor = kastor;
        this.kanez = Kanez.BORRADOR;
    }

    public static Caraxes crear(String id,
                                String azantysId,
                                Vala vala,
                                Sete sete,
                                Jorva jorva,
                                Urnebion urnebion,
                                CaraxesKastor kastor){
        if (id == null || id.isBlank()) throw new ReglaDominioException("El id del Caraxes es obligatorio");
        if (azantysId == null || azantysId.isBlank()) throw new ReglaDominioException("El id del Azantys es obligatorio");
        if (vala == null) throw  new ReglaDominioException("El Vala no puede ser nulo.");
        if (sete == null) throw new ReglaDominioException("El Sete no puede ser nulo.");
        if (jorva == null) throw new ReglaDominioException("El Jorva no puede ser nulo.");
        if (urnebion == null) throw new ReglaDominioException("El Urnebion no puede ser nulo.");
        if (kastor == null) throw new ReglaDominioException("El Kastor no puede ser nulo.");
        return new Caraxes(id, azantysId, vala, sete, jorva, urnebion, kastor);
    }

    public void publicarParaExhibicion() {
        if (kanez != Kanez.BORRADOR)
            throw new ReglaDominioException("Solo un Caraxes en BORRADOR puede publicarse para exhibición");

        this.kanez = Kanez.EXHIBICION;
    }

    public void publicarParaVenta() {
        if (kanez != Kanez.BORRADOR && kanez != Kanez.EXHIBICION)
            throw new ReglaDominioException("Solo un Caraxes en BORRADOR o EXHIBICION puede ponerse en venta");
        if (vala.monto() <= 0)
            throw new ReglaDominioException("Un Caraxes debe tener un precio mayor a cero para ponerse en venta");

        this.kanez = Kanez.EN_VENTA;
    }

    public void marcarComoVendido() {
        if (kanez != Kanez.EN_VENTA)
            throw new ReglaDominioException("Solo un Caraxes EN_VENTA puede marcarse como vendido");

        this.kanez = Kanez.VENDIDO;
    }

    public void retirar() {
        if (kanez == Kanez.VENDIDO)
            throw new ReglaDominioException("Un Caraxes VENDIDO no puede retirarse");
        if (kanez == Kanez.RETIRADO)
            throw new ReglaDominioException("El Caraxes ya se encuentra RETIRADO");

        this.kanez = Kanez.RETIRADO;
    }

    public void publicar(Kanez kanez) {
        if (kanez == null)
            throw new ReglaDominioException("El destino de publicación es obligatorio");

        switch (kanez) {
            case EXHIBICION -> publicarParaExhibicion();
            case EN_VENTA -> publicarParaVenta();
            default -> throw new ReglaDominioException(
                    "Solo se puede publicar para EXHIBICION o EN_VENTA");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Caraxes caraxes = (Caraxes) o;
        return Objects.equals(id, caraxes.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
