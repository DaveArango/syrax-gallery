package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: POST /api/zentys
public record RegistrarZentysRequest(
        @NotBlank(message = "El nombre del Zentys es obligatorio.")
        String nombre,

        @NotBlank(message = "El runiapo del Zentys es obligatorio.")
        String runiapos,

        @NotBlank(message = "El indior del Zentys es obligatorio")
        String indior
) {
}
