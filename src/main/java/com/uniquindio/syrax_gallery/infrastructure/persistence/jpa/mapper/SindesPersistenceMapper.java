package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SindesEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ValaEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class SindesPersistenceMapper {

    public SindesEntity toEntity(Sindes sindes) {
        SindesEntity entity = new SindesEntity();
        entity.setId(sindes.getId());
        entity.setIdCaraxes(sindes.getIdCaraxes());
        entity.setIdAzantys(sindes.getIdAzantys());
        entity.setIdZentys(sindes.getIdZentys());
        entity.setValaCongelado(new ValaEmbeddable(
                sindes.getValaCongelado().monto(),
                sindes.getValaCongelado().divisa()));
        entity.setFecha(sindes.getFecha());
        entity.setSindesJeda(sindes.getSindesJeda());
        return entity;
    }

    public Sindes toDomain(SindesEntity entity) {
        return Sindes.reconstruir(
                entity.getId(),
                entity.getIdCaraxes(),
                entity.getIdAzantys(),
                entity.getIdZentys(),
                new Vala(entity.getValaCongelado().getMonto(), entity.getValaCongelado().getDivisa()),
                entity.getFecha(),
                entity.getSindesJeda());
    }
}