package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.CaraxesKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
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
                    Kanez kanez,
                    Urnebion urnebion,
                    CaraxesKastor kastor){
        this.id = id;
        this.azantysId = azantysId;
        this.vala = vala;
        this.sete = sete;
        this.jorva = jorva;
        this.kanez = kanez;
        this.urnebion = urnebion;
        this.kastor = kastor;
    }

    public static Caraxes crear(String id,
                                String azantysId,
                                Vala vala,
                                Sete sete,
                                Jorva jorva,
                                Kanez kanez,
                                Urnebion urnebion,
                                CaraxesKastor kastor){
        if (id == null || id.isBlank()) throw new ReglaDominioException("El id del Caraxes es obligatorio");
        if (azantysId == null || azantysId.isBlank()) throw new ReglaDominioException("El id del Azantys es obligatorio");
        if (vala == null) throw  new ReglaDominioException("El Vala no puede ser nulo.");
        if (sete == null) throw new ReglaDominioException("El Sete no puede ser nulo.");
        if (jorva == null) throw new ReglaDominioException("El Jorva no puede ser nulo.");
        if (kanez == null) throw new ReglaDominioException("El Kanez no puede ser nulo.");
        if (urnebion == null) throw new ReglaDominioException("El Urnebion no puede ser nulo.");
        if (kastor == null) throw new ReglaDominioException("El Kastor no puede ser nulo.");
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
