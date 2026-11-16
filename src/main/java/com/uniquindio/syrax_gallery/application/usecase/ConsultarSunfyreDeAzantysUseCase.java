package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarSunfyreDeAzantysUseCase {

    private final SunfyreRepository repository;

    @Transactional(readOnly = true)
    public List<Sunfyre> ejecutar(String azantysId) {
        return repository.obtenerDeAzantys(azantysId);
    }
}
