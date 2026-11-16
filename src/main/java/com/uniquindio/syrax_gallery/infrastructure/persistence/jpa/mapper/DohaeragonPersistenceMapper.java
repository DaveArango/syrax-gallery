package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.AlionEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.DohaeragonEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ValaEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class DohaeragonPersistenceMapper {

    public DohaeragonEntity toEntity(Dohaeragon dohaeragon) {
        DohaeragonEntity entity = new DohaeragonEntity();
        entity.setId(dohaeragon.getId());
        entity.setIdAzantys(dohaeragon.getIdAzantys());
        entity.setIdZentys(dohaeragon.getIdZentys());
        entity.setSariInicio(dohaeragon.getSari().inicio());
        entity.setSariFin(dohaeragon.getSari().fin());
        entity.setKastor(dohaeragon.getKastor());
        entity.setAlion(new AlionEmbeddable(
                dohaeragon.getAlion().ciudad(),
                dohaeragon.getAlion().pais(),
                dohaeragon.getAlion().latitud(),
                dohaeragon.getAlion().longitud()));
        entity.setValaCongelado(new ValaEmbeddable(
                dohaeragon.getValaCongelado().monto(),
                dohaeragon.getValaCongelado().divisa()));
        entity.setDohaeroxJeda(dohaeragon.getDohaeroxJeda());
        entity.setKanez(dohaeragon.getKanez());
        return entity;
    }

    public Dohaeragon toDomain(DohaeragonEntity entity) {
        return Dohaeragon.reconstruir(
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
