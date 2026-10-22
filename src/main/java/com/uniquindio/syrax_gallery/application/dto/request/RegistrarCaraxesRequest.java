package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


// Mapea a: POST /api/caraxes
public record RegistrarCaraxesRequest(
        @NotBlank(message = "El id del Azantys es obligatorio.")
        String azantysId,

        @NotNull(message = "El monto del Vala es obligatorio.")
        Double monto,

        @NotBlank(message = "La divisa del Vala es obligatoria.")
        String divisa,

        @NotBlank(message = "El sete del Caraxes es obligatorio.")
        String sete,

        @NotBlank(message = "El jorva del Caraxes es obligatorio.")
        String jorva,

        @NotBlank(message = "El urnebion del Caraxes es obligatorio.")
        String urnebion,

        @NotBlank(message = "El kastor del Caraxes es obligatorio.")
        String kastor
) {}
