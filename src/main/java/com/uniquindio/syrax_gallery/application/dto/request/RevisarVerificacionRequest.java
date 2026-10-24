package com.uniquindio.syrax_gallery.application.dto.request;

import jakarta.validation.constraints.NotNull;

// Mapea a: PUT /api/azantys/{id}/revision
public record RevisarVerificacionRequest(
        @NotNull(message = "Debe indicar si la identidad se aprueba o se rechaza.")
        Boolean aprobada,

        String motivo
) {}
