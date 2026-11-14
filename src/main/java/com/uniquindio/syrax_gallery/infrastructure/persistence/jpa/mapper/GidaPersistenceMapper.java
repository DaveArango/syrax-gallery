package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Gida;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.GidaEntity;
import org.springframework.stereotype.Component;

@Component
public class GidaPersistenceMapper {

    public GidaEntity toEntity(Gida gida) {
        GidaEntity entity = new GidaEntity();
        entity.setId(gida.getId());
        entity.setIdZentys(gida.getIdZentys());
        entity.setIdCaraxes(gida.getIdCaraxes());
        entity.setTexto(gida.getTexto());
        entity.setFecha(gida.getFecha());
        return entity;
    }

    public Gida toDomain(GidaEntity entity) {
        return Gida.reconstruir(
                entity.getId(),
                entity.getIdZentys(),
                entity.getIdCaraxes(),
                entity.getTexto(),
                entity.getFecha());
    }
}