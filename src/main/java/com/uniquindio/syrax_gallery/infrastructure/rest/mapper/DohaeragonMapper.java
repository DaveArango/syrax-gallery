package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.DohaeragonDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import org.springframework.stereotype.Component;

@Component
public class DohaeragonMapper {

    public DohaeragonDetalleResponse toDetalleResponse(Dohaeragon dohaeragon) {
        return new DohaeragonDetalleResponse(
                dohaeragon.getId(),
                dohaeragon.getIdAzantys(),
                dohaeragon.getIdZentys(),
                dohaeragon.getSari().inicio(),
                dohaeragon.getSari().fin(),
                dohaeragon.getKastor().name(),
                dohaeragon.getAlion().ciudad(),
                dohaeragon.getAlion().pais(),
                dohaeragon.getValaCongelado().monto(),
                dohaeragon.getValaCongelado().divisa(),
                dohaeragon.getDohaeroxJeda().name(),
                dohaeragon.getKanez().name()
        );
    }
}

