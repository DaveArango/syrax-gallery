package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RetirarCaraxesUseCase {

    private final CaraxesRepository repository;

    @Transactional
    public Caraxes ejecutar(String azantysId, String caraxesId) {
        Caraxes caraxes = repository.obtener(caraxesId)
                .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"));

        caraxes.validarAutoria(azantysId);
        caraxes.retirar();

        repository.guardar(caraxes);
        return caraxes;
    }
}
