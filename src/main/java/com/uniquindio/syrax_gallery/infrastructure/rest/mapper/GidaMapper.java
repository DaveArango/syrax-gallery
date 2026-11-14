package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.GidaResponse;
import com.uniquindio.syrax_gallery.domain.entity.Gida;
import org.springframework.stereotype.Component;

@Component
public class GidaMapper {

    public GidaResponse toResponse(Gida gida) {
        return new GidaResponse(
                gida.getId(),
                gida.getIdZentys(),
                gida.getIdCaraxes(),
                gida.getTexto(),
                gida.getFecha()
        );
    }
}
