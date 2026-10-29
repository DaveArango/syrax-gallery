package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

// Mapea a: PUT /api/sunfyre/{id}/reprogramar   (el Azantys sale del token)
public record ReprogramarSunfyreRequest(
        @NotNull(message = "El nuevo inicio es obligatorio.")
        LocalDateTime inicio,

        @NotNull(message = "El nuevo fin es obligatorio.")
        LocalDateTime fin
) {}
