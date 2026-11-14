package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarMisSindesUseCase {

    private final SindesRepository sindesRepository;

    @Transactional(readOnly = true)
    public List<Sindes> ejecutar(String zentysId) {
        return sindesRepository.obtenerPorZentys(zentysId);
    }
}