package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// Mapea a: POST /api/azantys (registro público)
public record RegistrarAzantysRequest(

        @NotBlank(message = "El nombre del Azantys es obligatorio.")
        String nombre,

        @NotBlank(message = "La especialidad del Azantys es obligatoria.")
        String kostion,

        @NotBlank(message = "El correo del Azantys es obligatorio.")
        String runiapos,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
                message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.")
        String clave
) {}