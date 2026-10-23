package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.repository.AzantysServicios;

public record AzantysRankingResponse(String azantysId, String nombre, long servicios) {

    public static AzantysRankingResponse desde(AzantysServicios fila) {
        return new AzantysRankingResponse(fila.azantysId(), fila.nombre(), fila.servicios());
    }
}
