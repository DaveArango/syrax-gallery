package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/caraxes/{id}/vender
public record VenderCaraxesRequest(
        @NotBlank(message = "El id del Zentys es obligatorio.")
        String zentysId
) {}
