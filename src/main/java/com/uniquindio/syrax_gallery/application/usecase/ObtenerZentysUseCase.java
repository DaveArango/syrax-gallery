package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ObtenerZentysUseCase {
    private final ZentysRepository repository;

    public Zentys ejecutar(String id) {
        return repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Zentys no encontrado"));
    }
}
