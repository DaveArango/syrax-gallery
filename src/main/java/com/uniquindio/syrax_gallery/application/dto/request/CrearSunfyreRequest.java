package com.uniquindio.syrax_gallery.application.dto.request;

import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CrearSunfyreRequest(

        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId,

        @NotBlank(message = "El id del Zentys es obligatorio.")
        String zentysId,

        @NotNull(message = "El inicio es obligatorio.")
        LocalDateTime inicio,

        @NotNull(message = "El fin es obligatorio.")
        LocalDateTime fin,

        @NotNull(message = "El tipo de servicio es obligatorio.")
        SunfyreKastor kastor,

        @NotBlank(message = "La ciudad es obligatoria.")
        String ciudad,

        @NotBlank(message = "El país es obligatorio.")
        String pais,

        double latitud,
        double longitud,
        double monto,

        @NotBlank(message = "La divisa es obligatoria.")
        String divisa
) {}
