package com.uniquindio.syrax_gallery.application.dto.response;

import java.time.LocalDateTime;

public record SindesDetalleResponse(
        String id,
        String idCaraxes,
        String idAzantys,
        String idZentys,
        double monto,
        String divisa,
        LocalDateTime fecha,
        String estado
) {}