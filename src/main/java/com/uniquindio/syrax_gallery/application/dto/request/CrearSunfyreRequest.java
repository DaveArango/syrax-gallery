package com.uniquindio.syrax_gallery.application.dto.request;

import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

// Mapea a: POST /api/sunfyre   (el Azantys que lo ofrece sale del token)
public record CrearSunfyreRequest(
        @NotBlank(message = "El nombre del servicio es obligatorio.")
        String sete,

        @NotBlank(message = "La descripción del servicio es obligatoria.")
        String jorva,

        @NotBlank(message = "La imagen de referencia es obligatoria.")
        String urnebion,

        @NotNull(message = "El kastor (tipo de servicio) es obligatorio.")
        SunfyreKastor kastor,

        @NotBlank(message = "La ciudad es obligatoria.")
        String ciudad,

        @NotBlank(message = "El país es obligatorio.")
        String pais,

        // Opcionales: si no llegan, se guardan en 0
        Double latitud,
        Double longitud,

        @NotNull(message = "El monto del Vala es obligatorio.")
        @Positive(message = "El monto debe ser mayor a cero.")
        Double monto,

        @NotBlank(message = "La divisa del Vala es obligatoria.")
        String divisa,

        @NotNull(message = "La duración es obligatoria.")
        @Min(value = 30, message = "La duración mínima es de 30 minutos.")
        Integer duracionMinutos,

        @NotEmpty(message = "Debe indicar al menos un día de atención.")
        List<DayOfWeek> dias,

        @NotNull(message = "La hora de apertura es obligatoria.")
        LocalTime apertura,

        @NotNull(message = "La hora de cierre es obligatoria.")
        LocalTime cierre
) {}
