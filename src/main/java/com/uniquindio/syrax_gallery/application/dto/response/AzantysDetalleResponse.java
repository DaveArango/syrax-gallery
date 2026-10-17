package com.uniquindio.syrax_gallery.application.dto.response;

public record AzantysDetalleResponse(
        String id,
        String nombre,
        String kostion,
        String runiapos,
        boolean activo,
        boolean iksiaVerificada,
        boolean laehurlionVerificado
) {}
