package com.uniquindio.syrax_gallery.application.dto.response;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;

public record AzantysPerfilResponse(
        String id,
        String nombre,
        String kostion,
        String laehurlion
) {
    public static AzantysPerfilResponse desde(Azantys azantys) {
        return new AzantysPerfilResponse(
                azantys.getId(),
                azantys.getNombre(),
                azantys.getKostion().descripcion(),
                azantys.getLaehurlion() == null ? null : azantys.getLaehurlion().url());
    }
}
