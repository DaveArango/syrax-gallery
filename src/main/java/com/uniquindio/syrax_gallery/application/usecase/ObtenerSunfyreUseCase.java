package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ObtenerSunfyreUseCase {

    private final SunfyreRepository repository;

    @Transactional(readOnly = true)
    public Sunfyre ejecutar(String id, String solicitanteId) {
        Sunfyre sunfyre = repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));

        sunfyre.validarParticipante(solicitanteId);
        return sunfyre;
    }
}
