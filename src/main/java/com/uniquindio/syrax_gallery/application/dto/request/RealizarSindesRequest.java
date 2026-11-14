package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RealizarSindesRequest(
        @NotBlank(message = "El id del Caraxes es obligatorio.")
        String caraxesId
) {}