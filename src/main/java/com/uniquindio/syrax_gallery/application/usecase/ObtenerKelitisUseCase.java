package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ObtenerKelitisUseCase {

    private final KelitisRepository repository;

    @Transactional(readOnly = true)
    public Kelitis ejecutar(String id, String solicitanteId) {
        Kelitis kelitis = repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Kelitis no encontrado"));

        kelitis.validarParticipante(solicitanteId);
        return kelitis;
    }
}
