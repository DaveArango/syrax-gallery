package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConstituirKelitisRequest(
        @NotBlank(message = "El id de la Sunfyre es obligatorio.")
        String sunfyreId
) {}
