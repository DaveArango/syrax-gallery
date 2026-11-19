package com.uniquindio.syrax_gallery.application.dto.response;

import java.util.List;

// Mapea a: GET y PUT /api/{caraxes|sunfyre}/{id}/imagenes
public record ImagenesReferenciaResponse(List<String> urls) {}