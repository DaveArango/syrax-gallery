package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/sunfyre/{id}/finalizar
public record FinalizarSunfyreRequest(
        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId
) {}
