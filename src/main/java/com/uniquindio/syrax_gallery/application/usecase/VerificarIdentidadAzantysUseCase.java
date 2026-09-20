package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;

public class VerificarIdentidadAzantysUseCase {
    private final AzantysRepository repository;

    public VerificarIdentidadAzantysUseCase(AzantysRepository repository) {
        this.repository = repository;
    }

    public Azantys ejecutar(String id, Iksia iksia) {
        Azantys azantys = repository.obtener(id)
                .orElseThrow(() -> new IllegalArgumentException("Azantys no encontrado"));
        azantys.verificarIdentidad(iksia);
        repository.guardar(azantys);
        return azantys;
    }
}
