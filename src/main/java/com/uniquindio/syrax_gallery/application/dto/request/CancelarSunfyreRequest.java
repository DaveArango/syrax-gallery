package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/sunfyre/{id}/cancelar
public record CancelarSunfyreRequest(
        @NotBlank(message = "El id del solicitante es obligatorio.")
        String solicitanteId,

        @NotBlank(message = "El motivo de cancelación es obligatorio.")
        String motivo
) {}
