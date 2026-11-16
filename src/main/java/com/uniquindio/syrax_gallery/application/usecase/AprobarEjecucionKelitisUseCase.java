package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AprobarEjecucionKelitisUseCase {

    private final KelitisRepository kelitisRepository;
    private final DohaeragonRepository dohaeragonRepository;

    @Transactional
    public Kelitis ejecutar(String kelitisId, String zentysId) {
        Kelitis kelitis = kelitisRepository.obtener(kelitisId)
                .orElseThrow(() -> new NoSuchElementException("Kelitis no encontrado"));
        kelitis.validarCliente(zentysId);

        Dohaeragon dohaeragon = dohaeragonRepository.obtener(kelitis.getIdDohaeragon())
                .orElseThrow(() -> new NoSuchElementException("Dohaeragon no encontrada"));
        dohaeragon.validarCompletada();

        kelitis.liberarPago();
        kelitisRepository.guardar(kelitis);
        return kelitis;
    }
}
