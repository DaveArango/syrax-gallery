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
}
