package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;

public class ObtenerCaraxesUseCase {
    private final CaraxesRepository repository;

    public ObtenerCaraxesUseCase(CaraxesRepository repository) {
        this.repository = repository;
    }

    public Caraxes ejecutar(String id) {
        return repository.obtener(id)
                .orElseThrow(() -> new IllegalArgumentException("Caraxes no encontrado"));
    }
}