package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.CaraxesDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import org.springframework.stereotype.Component;

@Component
public class CaraxesMapper {

    public CaraxesDetalleResponse toDetalleResponse(Caraxes caraxes){
        return new CaraxesDetalleResponse(
                caraxes.getId(),
                caraxes.getAzantysId(),
                caraxes.getVala().monto(),
                caraxes.getVala().divisa(),
                caraxes.getSete().nombre(),
                caraxes.getJorva().descripcion(),
                caraxes.getUrnebion().url(),
                caraxes.getKanez().name(),
                caraxes.getKastor().name()

        );
    }

}
