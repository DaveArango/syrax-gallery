package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

// Mapea a: POST /api/sunfyre/{id}/reservas   (el Zentys que reserva sale del token)
public record ReservarSunfyreRequest(
        @NotNull(message = "La fecha y hora de inicio son obligatorias.")
        LocalDateTime inicio
) {}
