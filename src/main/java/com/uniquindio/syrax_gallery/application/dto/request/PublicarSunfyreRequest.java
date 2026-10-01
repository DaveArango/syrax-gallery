package com.uniquindio.syrax_gallery.application.dto.request;

import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PublicarSunfyreRequest(

        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId,

        @NotNull(message = "El destino de publicación es obligatorio.")
        Kanez destino
) {}
 

