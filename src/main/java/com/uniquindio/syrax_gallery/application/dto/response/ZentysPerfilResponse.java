package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;

public record ZentysPerfilResponse(
        String id,
        String nombre,
        String descripcion
) {
    public static ZentysPerfilResponse desde(Zentys zentys) {
        return new ZentysPerfilResponse(
                zentys.getId(),
                zentys.getNombre(),
                zentys.getIndior().descripcion());
    }
}
