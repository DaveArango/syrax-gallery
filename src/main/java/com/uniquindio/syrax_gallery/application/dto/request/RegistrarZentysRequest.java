package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Mapea a: POST /api/zentys (registro público)
public record RegistrarZentysRequest(
        @NotBlank(message = "El nombre del Zentys es obligatorio.")
        String nombre,

        @NotBlank(message = "El runiapo del Zentys es obligatorio.")
        String runiapos,

        @NotBlank(message = "El indior del Zentys es obligatorio")
        String indior,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
                message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.")
        String clave
) {
}
