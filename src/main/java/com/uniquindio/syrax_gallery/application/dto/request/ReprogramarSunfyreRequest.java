package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

// Mapea a: PUT /api/sunfyre/{id}/reprogramar
public record ReprogramarSunfyreRequest(
        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId,

        @NotNull(message = "El nuevo inicio es obligatorio.")
        LocalDateTime inicio,

        @NotNull(message = "El nuevo fin es obligatorio.")
        LocalDateTime fin
) {}
