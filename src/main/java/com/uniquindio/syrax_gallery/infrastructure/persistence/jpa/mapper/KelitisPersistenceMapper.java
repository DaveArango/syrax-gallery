package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.KelitisEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ValaEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class KelitisPersistenceMapper {

    public KelitisEntity toEntity(Kelitis kelitis) {
        KelitisEntity entity = new KelitisEntity();
        entity.setId(kelitis.getId());
        entity.setIdSunfyre(kelitis.getIdSunfyre());
        entity.setIdAzantys(kelitis.getIdAzantys());
        entity.setIdZentys(kelitis.getIdZentys());
        entity.setValaCongelado(new ValaEmbeddable(
                kelitis.getValaCongelado().monto(),
                kelitis.getValaCongelado().divisa()));
        entity.setGelior(kelitis.getGelior());
        return entity;
    }

    public Kelitis toDomain(KelitisEntity entity) {
        return Kelitis.reconstruir(
                entity.getId(),
                entity.getIdSunfyre(),
                entity.getIdAzantys(),
                entity.getIdZentys(),
                new Vala(entity.getValaCongelado().getMonto(), entity.getValaCongelado().getDivisa()),
                entity.getGelior());
    }
}
