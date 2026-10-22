package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.ZentysDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import org.springframework.stereotype.Component;

@Component
public class ZentysMapper {
    public ZentysDetalleResponse toDetalleResponse(Zentys zentys){
        return new ZentysDetalleResponse(
                zentys.getId(),
                zentys.getNombre(),
                zentys.getRuniapos().direccion(),
                zentys.getIndior().descripcion(),
                zentys.isEstaActivo()
        );
    }
}
