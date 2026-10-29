package com.uniquindio.syrax_gallery.application.dto.request;

import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import jakarta.validation.constraints.NotNull;

// Mapea a: PUT /api/sunfyre/{id}/publicar   (el Azantys sale del token)
public record PublicarSunfyreRequest(
        @NotNull(message = "El destino de publicación es obligatorio.")
        Kanez destino
) {}
 

