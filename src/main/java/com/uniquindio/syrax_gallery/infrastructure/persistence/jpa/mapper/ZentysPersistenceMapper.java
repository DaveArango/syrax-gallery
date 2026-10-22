package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.valueobject.Indior;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.IndiorEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.RuniaposEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ZentysEntity;
import org.springframework.stereotype.Component;

@Component
public class ZentysPersistenceMapper {

    public ZentysEntity toEntity(Zentys zentys){
        ZentysEntity entity = new ZentysEntity();

        entity.setId(zentys.getId());
        entity.setNombre(zentys.getNombre());
        entity.setRuniapos(
                new RuniaposEmbeddable(zentys.getRuniapos().direccion())
        );
        entity.setIndior(
                new IndiorEmbeddable(zentys.getIndior().descripcion())
        );
        entity.setEstaActivo(zentys.isEstaActivo());

        return entity;
    }

    public Zentys toDomain(ZentysEntity entity){
        return Zentys.reconstruir(entity.getId(),
                entity.getNombre(),
                new Runiapos(entity.getRuniapos().getDireccion()),
                new Indior(entity.getIndior().getDescripcion()),
                entity.isEstaActivo());
    }
}
