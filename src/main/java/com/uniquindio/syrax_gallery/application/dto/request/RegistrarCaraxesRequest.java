package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


// Mapea a: POST /api/caraxes
public record RegistrarCaraxesRequest(
        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId
) {}
