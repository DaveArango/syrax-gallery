package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ObtenerCaraxesUseCase {
    private final CaraxesRepository repository;

    @Transactional(readOnly = true)
    public Caraxes ejecutar(String id) {
        return repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"));
    }
}