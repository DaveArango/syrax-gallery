package com.uniquindio.syrax_gallery.application.dto.response;

// Mapea a: GET /caraxes/{id}
public record CaraxesDetalleResponse(
    String id,
    String azantysId,
    double monto,
    String divisa,
    String sete,
    String jorva,
    String urnebion,
    String kanez,
    String kastor
){}
