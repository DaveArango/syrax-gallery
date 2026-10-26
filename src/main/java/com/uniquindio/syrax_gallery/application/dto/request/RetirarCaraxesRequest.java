package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/caraxes/{id}/retirar
public record RetirarCaraxesRequest(
        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId
) {}
