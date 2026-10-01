package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.AzantysDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import org.springframework.stereotype.Component;

@Component
public class AzantysMapper {

    public AzantysDetalleResponse toDetalleResponse(Azantys azantys) {

        return new AzantysDetalleResponse(
                azantys.getId(),
                azantys.getNombre(),
                azantys.getKostion().descripcion(),
                azantys.getRuniapos().direccion(),
                azantys.getLentorId(),
                azantys.isActivo(),
                azantys.isIksiaVerificada(),
                azantys.isLaehurlionVerificado()
        );
    }
}
