package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.valueobject.Jorva;
import com.uniquindio.syrax_gallery.domain.valueobject.Sete;
import com.uniquindio.syrax_gallery.domain.valueobject.Urnebion;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.*;
import org.springframework.stereotype.Component;

@Component
public class CaraxesPersistenceMapper {

    public CaraxesEntity toEntity(Caraxes caraxes){
        CaraxesEntity entity = new CaraxesEntity();

        entity.setId(caraxes.getId());
        entity.setIdAzantys(caraxes.getIdAzantys());
        entity.setVala(
                new ValaEmbeddable(caraxes.getVala().monto(),
                        caraxes.getVala().divisa())
        );
        entity.setSete(
                new SeteEmbeddable(caraxes.getSete().nombre())
        );
        entity.setJorva(
                new JorvaEmbeddable(caraxes.getJorva().descripcion())
        );
        entity.setKanez(caraxes.getKanez());
        entity.setUrnebion(
                new UrnebionEmbeddable(caraxes.getUrnebion().url())
        );
        entity.setKastor(caraxes.getKastor());

        return entity;
    }

    public Caraxes toDomain(CaraxesEntity entity){
        return Caraxes.reconstruir(
                entity.getId(),
                entity.getIdAzantys(),
                new Vala(entity.getVala().getMonto(), entity.getVala().getDivisa()),
                new Sete(entity.getSete().getNombre()),
                new Jorva(entity.getJorva().getDescripcion()),
                new Urnebion(entity.getUrnebion().getUrl()),
                entity.getKastor(),
                entity.getKanez()
        );
    }
}
