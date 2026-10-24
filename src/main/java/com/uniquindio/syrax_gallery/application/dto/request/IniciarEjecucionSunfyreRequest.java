package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/sunfyre/{id}/iniciar
public record IniciarEjecucionSunfyreRequest(
        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId
) {}
