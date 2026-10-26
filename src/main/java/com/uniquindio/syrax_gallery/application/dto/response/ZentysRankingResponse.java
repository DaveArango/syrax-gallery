package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.repository.ZentysServicios;

public record ZentysRankingResponse(
        String zentysId,
        String nombre,
        long servicios
) {
    public static ZentysRankingResponse desde(ZentysServicios fila) {
        return new ZentysRankingResponse(fila.zentysId(),
                fila.nombre(),
                fila.servicios());
    }
}
