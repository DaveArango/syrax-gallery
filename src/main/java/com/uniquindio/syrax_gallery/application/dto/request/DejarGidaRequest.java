package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DejarGidaRequest(
        @NotBlank(message = "El id del Caraxes es obligatorio.")
        String caraxesId,

        @NotBlank(message = "El texto del Gida es obligatorio.")
        @Size(max = 500, message = "El Gida no puede superar 500 caracteres.")
        String texto
) {}