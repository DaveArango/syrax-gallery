package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarSindesDeAzantysUseCase {

    private final SindesRepository repository;

    // Las compras de las obras del Azantys: las PENDIENTE son las que le falta confirmar
    @Transactional(readOnly = true)
    public List<Sindes> ejecutar(String azantysId) {
        return repository.obtenerPorAzantys(azantysId);
    }
}
