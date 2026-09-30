package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


// Mapea a: POST /caraxes
public record RegistrarCaraxesRequest(
        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId,

        @NotNull(message = "El monto del Vala es obligatorio.")
        double monto,

        @NotBlank(message = "La divisa del Vala es obligatoria.")
        String divisa,

        @NotBlank(message = "El Sete del Caraxes es obligatorio.")
        String sete,

        @NotBlank(message = "El Jorva del Caraxes es obligatorio.")
        String jorva,

        @NotBlank(message = "El Urnebion del Caraxes es obligatorio.")
        String urnebion,

        @NotNull(message = "El Kastor del Caraxes es obligatorio.")
        String kastor
) {}
