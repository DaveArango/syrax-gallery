package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Jedar;
import com.uniquindio.syrax_gallery.domain.valueobject.Jorva;
import com.uniquindio.syrax_gallery.domain.valueobject.Sete;
import com.uniquindio.syrax_gallery.domain.valueobject.Urnebion;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.AlionEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SunfyreEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.JorvaEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SeteEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.UrnebionEmbeddable;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ValaEmbeddable;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SunfyrePersistenceMapper {

    public SunfyreEntity toEntity(Sunfyre sunfyre) {
        SunfyreEntity entity = new SunfyreEntity();
        entity.setId(sunfyre.getId());
        entity.setIdAzantys(sunfyre.getIdAzantys());
        entity.setSete(new SeteEmbeddable(sunfyre.getSete().nombre()));
        entity.setJorva(new JorvaEmbeddable(sunfyre.getJorva().descripcion()));
        entity.setUrnebion(new UrnebionEmbeddable(sunfyre.getUrnebion().url()));
        entity.setKastor(sunfyre.getKastor());
        entity.setAlion(new AlionEmbeddable(
                sunfyre.getAlion().ciudad(),
                sunfyre.getAlion().pais(),
                sunfyre.getAlion().latitud(),
                sunfyre.getAlion().longitud()));
        entity.setVala(new ValaEmbeddable(sunfyre.getVala().monto(), sunfyre.getVala().divisa()));
        entity.setDuracionMinutos(sunfyre.getDuracionMinutos());
        entity.setDias(sunfyre.getJedar().dias().stream()
                .sorted()
                .map(DayOfWeek::name)
                .collect(Collectors.joining(",")));
        entity.setApertura(sunfyre.getJedar().apertura());
        entity.setCierre(sunfyre.getJedar().cierre());
        entity.setActivo(sunfyre.isActivo());
        return entity;
    }

    public Sunfyre toDomain(SunfyreEntity entity) {
        Set<DayOfWeek> dias = Arrays.stream(entity.getDias().split(","))
                .map(String::trim)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());

        return Sunfyre.reconstruir(
                entity.getId(),
                entity.getIdAzantys(),
                new Sete(entity.getSete().getNombre()),
                new Jorva(entity.getJorva().getDescripcion()),
                new Urnebion(entity.getUrnebion().getUrl()),
                entity.getKastor(),
                new Alion(entity.getAlion().getCiudad(), entity.getAlion().getPais(),
                        entity.getAlion().getLatitud(), entity.getAlion().getLongitud()),
                new Vala(entity.getVala().getMonto(), entity.getVala().getDivisa()),
                entity.getDuracionMinutos(),
                new Jedar(dias, entity.getApertura(), entity.getCierre()),
                entity.isActivo());
    }
}
