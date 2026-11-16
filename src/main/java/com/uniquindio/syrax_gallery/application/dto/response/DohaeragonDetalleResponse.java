package com.uniquindio.syrax_gallery.application.dto.response;

import java.time.LocalDateTime;

public record DohaeragonDetalleResponse(
        String id,
        String idAzantys,
        String idZentys,
        LocalDateTime inicio,
        LocalDateTime fin,
        String kastor,
        String ciudad,
        String pais,
        double monto,
        String divisa,
        String estado,
        String kanez,
        String idSunfyre   // null si la cita la creó el Azantys
) {}
