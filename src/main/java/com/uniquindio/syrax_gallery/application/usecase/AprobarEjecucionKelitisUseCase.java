package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AprobarEjecucionKelitisUseCase {

    private final KelitisRepository kelitisRepository;
    private final SunfyreRepository sunfyreRepository;

    @Transactional
    public Kelitis ejecutar(String kelitisId, String zentysId) {
        Kelitis kelitis = kelitisRepository.obtener(kelitisId)
                .orElseThrow(() -> new NoSuchElementException("Kelitis no encontrado"));
        kelitis.validarCliente(zentysId);

        Sunfyre sunfyre = sunfyreRepository.obtener(kelitis.getIdSunfyre())
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));
        sunfyre.validarCompletada();

        kelitis.liberarPago();
        kelitisRepository.guardar(kelitis);
        return kelitis;
    }
}
