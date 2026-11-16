package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ObtenerDohaeragonUseCase {

    private final DohaeragonRepository repository;

    @Transactional(readOnly = true)
    public Dohaeragon ejecutar(String id, String solicitanteId) {
        Dohaeragon dohaeragon = repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Dohaeragon no encontrada"));

        dohaeragon.validarParticipante(solicitanteId);
        return dohaeragon;
    }
}
