package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;

public record AzantysVerificacionResponse(
        String id,
        String nombre,
        String laehurlion,
        String estado
) {
    public static AzantysVerificacionResponse desde(Azantys azantys) {
        return new AzantysVerificacionResponse(
                azantys.getId(),
                azantys.getNombre(),
                azantys.getLaehurlion() == null ? null : azantys.getLaehurlion().url(),
                estadoDe(azantys));
    }

    private static String estadoDe(Azantys azantys) {
        if (azantys.estaHabilitadoParaPublicar()) return "APROBADA";
        if (azantys.estaPendienteDeRevision()) return "PENDIENTE";
        if (azantys.isLaehurlionVerificado()) return "RECHAZADA";
        return "SIN_ENVIAR";
    }
}