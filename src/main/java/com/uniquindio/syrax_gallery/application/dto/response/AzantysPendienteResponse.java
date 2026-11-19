package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;

// Mapea a: GET /api/azantys/pendientes (solo ADMIN)
public record AzantysPendienteResponse(
        String id,
        String nombre,
        String kostion,
        String runiapos,
        String iksia,
        String laehurlion
) {
    public static AzantysPendienteResponse desde(Azantys azantys) {
        // Un Azantys pendiente siempre tiene documento y foto cargados
        return new AzantysPendienteResponse(
                azantys.getId(),
                azantys.getNombre(),
                azantys.getKostion().descripcion(),
                azantys.getRuniapos().direccion(),
                azantys.getIksia().numero(),
                azantys.getLaehurlion().url());
    }
}

