package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ObtenerSunfyreUseCase {

    private final SunfyreRepository repository;

    public ObtenerSunfyreUseCase(SunfyreRepository repository) {
        this.repository = repository;
    }

    public Sunfyre ejecutar(String id) {
        return repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));
    }
}
