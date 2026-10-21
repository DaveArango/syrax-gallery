package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

// Mapea a: PUT /api/azantys/{id}/identidad
public record VerificarIdentidadRequest(

        @NotBlank(message = "El número de documento es obligatorio.")
        String iksia,

        @NotBlank(message = "La foto de perfil es obligatoria.")
        String laehurlion
) {}
