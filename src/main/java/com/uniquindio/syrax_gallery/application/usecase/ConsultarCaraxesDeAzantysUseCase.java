package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarCaraxesDeAzantysUseCase {

    private final CaraxesRepository repository;

    // Todas las obras del Azantys, también las que están en BORRADOR
    @Transactional(readOnly = true)
    public List<Caraxes> ejecutar(String azantysId) {
        return repository.obtenerDeAzantys(azantysId);
    }
}
