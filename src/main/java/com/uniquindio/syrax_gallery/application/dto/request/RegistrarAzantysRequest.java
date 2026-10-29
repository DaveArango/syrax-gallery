package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Mapea a: POST /api/azantys (registro público)
public record RegistrarAzantysRequest(

        @NotBlank(message = "El nombre del Azantys es obligatorio.")
        String nombre,

        @NotBlank(message = "La especialidad del Azantys es obligatoria.")
        String kostion,

        @NotBlank(message = "El correo del Azantys es obligatorio.")
        String runiapos,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
        String clave
) {}