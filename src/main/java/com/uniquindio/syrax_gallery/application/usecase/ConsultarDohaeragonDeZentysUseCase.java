package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarDohaeragonDeZentysUseCase {

    private final DohaeragonRepository repository;

    // Las reservas del Zentys: las próximas para recordarlas y las pasadas como historial
    @Transactional(readOnly = true)
    public List<Dohaeragon> ejecutar(String zentysId) {
        return repository.obtenerDeZentys(zentysId);
    }
}
