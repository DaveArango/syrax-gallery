package com.uniquindio.syrax_gallery.application.dto.response;

import java.time.LocalDateTime;

// Una hora libre de una Sunfyre: el front manda "inicio" tal cual al reservar
public record VentanaResponse(
        LocalDateTime inicio,
        LocalDateTime fin
) {}
