package com.uniquindio.syrax_gallery.application.dto.response;

import java.time.LocalDateTime;

public record GidaResponse(
        String id,
        String idZentys,
        String idCaraxes,
        String texto,
        LocalDateTime fecha
) {}