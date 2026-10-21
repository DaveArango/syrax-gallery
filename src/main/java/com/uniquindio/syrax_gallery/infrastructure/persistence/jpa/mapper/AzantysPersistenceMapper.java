package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Laehurlion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.AzantysEntity;
import org.springframework.stereotype.Component;

@Component
public class AzantysPersistenceMapper {

    public AzantysEntity toEntity(Azantys azantys) {
        AzantysEntity entity = new AzantysEntity();
        entity.setId(azantys.getId());
        entity.setNombre(azantys.getNombre());
        entity.setActivo(azantys.isActivo());
        entity.setKostion(azantys.getKostion().descripcion());
        entity.setRuniapos(azantys.getRuniapos().direccion());
        entity.setIksia(azantys.getIksia() == null ? null : azantys.getIksia().numero());
        entity.setIksiaVerificada(azantys.isIksiaVerificada());
        entity.setLaehurlion(azantys.getLaehurlion() == null ? null : azantys.getLaehurlion().url());
        entity.setLaehurlionVerificado(azantys.isLaehurlionVerificado());
        return entity;
    }

    public Azantys toDomain(AzantysEntity entity) {
        return Azantys.reconstruir(
                entity.getId(),
                entity.getNombre(),
                entity.isActivo(),
                new Kostion(entity.getKostion()),
                new Runiapos(entity.getRuniapos()),
                entity.getIksia() == null ? null : new Iksia(entity.getIksia()),
                entity.isIksiaVerificada(),
                entity.getLaehurlion() == null ? null : new Laehurlion(entity.getLaehurlion()),
                entity.isLaehurlionVerificado());
    }
}
