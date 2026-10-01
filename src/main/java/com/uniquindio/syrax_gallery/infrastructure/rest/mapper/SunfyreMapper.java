package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.SunfyreDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import org.springframework.stereotype.Component;

@Component
public class SunfyreMapper {

    public SunfyreDetalleResponse toDetalleResponse(Sunfyre sunfyre) {
        return new SunfyreDetalleResponse(
                sunfyre.getId(),
                sunfyre.getIdAzantys(),
                sunfyre.getIdZentys(),
                sunfyre.getSari().inicio(),
                sunfyre.getSari().fin(),
                sunfyre.getKastor().name(),
                sunfyre.getAlion().ciudad(),
                sunfyre.getAlion().pais(),
                sunfyre.getValaCongelado().monto(),
                sunfyre.getValaCongelado().divisa(),
                sunfyre.getDohaeroxJeda().name(),
                sunfyre.getKanez().name()
        );
    }
}

