package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegistrarAzantysRequest(

        @NotBlank(message = "El id del Azantys es obligatorio.")
        String id,

        @NotBlank(message = "El nombre del Azantys es obligatorio.")
        String nombre,

        @NotBlank(message = "La especialidad del Azantys es obligatoria.")
        String kostion,

        @NotBlank(message = "El email del Azantys es obligatorio.")
        String runiapos,

        String lentorId
) {}