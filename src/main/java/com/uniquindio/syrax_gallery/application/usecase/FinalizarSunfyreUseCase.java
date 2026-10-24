package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FinalizarSunfyreUseCase {

    private final SunfyreRepository repository;

    @Transactional
    public Sunfyre ejecutar(String azantysId, String sunfyreId) {
        Sunfyre sunfyre = repository.obtener(sunfyreId)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));

        sunfyre.validarAutoria(azantysId);
        sunfyre.finalizar();

        repository.guardar(sunfyre);
        return sunfyre;
    }
}
