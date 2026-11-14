package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CancelarSindesUseCase {

    private final SindesRepository sindesRepository;

    @Transactional
    public Sindes ejecutar(String sindesId, String zentysId) {
        Sindes sindes = sindesRepository.obtener(sindesId)
                .orElseThrow(() -> new NoSuchElementException("Sindes no encontrado"));

        sindes.validarZentys(zentysId);
        sindes.cancelar();

        sindesRepository.guardar(sindes);
        return sindes;
    }
}