package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/kelitis/{id}/aprobar
public record AprobarEjecucionKelitisRequest(
        @NotBlank(message = "El id del Zentys es obligatorio.")
        String zentysId
) {}
