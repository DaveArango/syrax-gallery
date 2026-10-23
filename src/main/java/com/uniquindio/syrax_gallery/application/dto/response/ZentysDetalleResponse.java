package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;

// Mapea a: GET /api/zentys/{id}
public record ZentysDetalleResponse(
        String id,
        String nombre,
        String direccion,
        String descripcion,
        boolean activo
) {

    public static ZentysDetalleResponse desde(Zentys zentys){
        return new ZentysDetalleResponse(
                zentys.getId(),
                zentys.getNombre(),
                zentys.getRuniapos().direccion(),
                zentys.getIndior().descripcion(),
                zentys.isEstaActivo()
        );
    }
}
