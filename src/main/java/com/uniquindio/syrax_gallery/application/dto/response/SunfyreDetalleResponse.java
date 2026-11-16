package com.uniquindio.syrax_gallery.application.dto.response;

import java.util.List;

public record SunfyreDetalleResponse(
        String id,
        String idAzantys,
        String sete,
        String jorva,
        String urnebion,
        String kastor,
        String ciudad,
        String pais,
        double monto,
        String divisa,
        int duracionMinutos,
        List<String> dias,      // MONDAY, TUESDAY...
        String apertura,        // HH:mm
        String cierre,          // HH:mm
        boolean activo
) {}
