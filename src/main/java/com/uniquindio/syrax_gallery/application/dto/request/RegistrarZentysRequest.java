package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: POST /api/zentys
public record RegistrarZentysRequest(
        @NotBlank(message = "El id del Zentys es obligatorio.")
        String zentysId,

        @NotBlank(message = "El nombre del Zentys es obligatorio.")
        String nombre
) {
}
