package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/dohaeragon/{id}/cancelar
public record CancelarDohaeragonRequest(
        @NotBlank(message = "El motivo de cancelación es obligatorio.")
        String motivo
) {}
