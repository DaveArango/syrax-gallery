package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.AlionEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SunfyreEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ValaEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class SunfyrePersistenceMapper {

    public SunfyreEntity toEntity(Sunfyre sunfyre) {
        SunfyreEntity entity = new SunfyreEntity();
        entity.setId(sunfyre.getId());
        entity.setIdAzantys(sunfyre.getIdAzantys());
        entity.setIdZentys(sunfyre.getIdZentys());
        entity.setSariInicio(sunfyre.getSari().inicio());
        entity.setSariFin(sunfyre.getSari().fin());
        entity.setKastor(sunfyre.getKastor());
        entity.setAlion(new AlionEmbeddable(
                sunfyre.getAlion().ciudad(),
                sunfyre.getAlion().pais(),
                sunfyre.getAlion().latitud(),
                sunfyre.getAlion().longitud()));
        entity.setValaCongelado(new ValaEmbeddable(
                sunfyre.getValaCongelado().monto(),
                sunfyre.getValaCongelado().divisa()));
        entity.setDohaeroxJeda(sunfyre.getDohaeroxJeda());
        entity.setKanez(sunfyre.getKanez());
        return entity;
    }

    public Sunfyre toDomain(SunfyreEntity entity) {
        return Sunfyre.reconstruir(
                entity.getId(),
                entity.getIdAzantys(),
                entity.getIdZentys(),
                new Sari(entity.getSariInicio(), entity.getSariFin()),
                entity.getKastor(),
                new Alion(entity.getAlion().getCiudad(), entity.getAlion().getPais(),
                        entity.getAlion().getLatitud(), entity.getAlion().getLongitud()),
                new Vala(entity.getValaCongelado().getMonto(), entity.getValaCongelado().getDivisa()),
                entity.getDohaeroxJeda(),
                entity.getKanez());
    }
}
