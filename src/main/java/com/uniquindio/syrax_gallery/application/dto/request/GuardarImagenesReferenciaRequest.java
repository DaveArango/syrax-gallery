package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record GuardarImagenesReferenciaRequest(
        @NotNull(message = "La lista de imágenes es obligatoria.")
        @Size(max = 5, message = "Puedes agregar máximo 5 imágenes de referencia adicionales.")
        List<@NotBlank(message = "Las imágenes no pueden venir vacías.")
        @Size(max = 500, message = "El enlace de una imagen es demasiado largo.")
        @Pattern(regexp = "^https?://.+", message = "Cada imagen debe ser un enlace http o https.")
                String> urls
) {}