package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FinalizarDohaeragonUseCase {

    private final DohaeragonRepository repository;

    @Transactional
    public Dohaeragon ejecutar(String azantysId, String dohaeragonId) {
        Dohaeragon dohaeragon = repository.obtener(dohaeragonId)
                .orElseThrow(() -> new NoSuchElementException("Dohaeragon no encontrada"));

        dohaeragon.validarAutoria(azantysId);
        dohaeragon.finalizar();

        repository.guardar(dohaeragon);
        return dohaeragon;
    }
}
