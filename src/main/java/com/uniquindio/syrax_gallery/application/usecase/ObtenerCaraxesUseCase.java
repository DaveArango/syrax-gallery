package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObtenerCaraxesUseCase {
    private final CaraxesRepository repository;

    public Caraxes ejecutar(String id) {
        return repository.obtener(id)
                .orElseThrow(() -> new IllegalArgumentException("Caraxes no encontrado"));
    }
}