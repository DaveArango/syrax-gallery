package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CancelarDohaeragonUseCase {

    private final DohaeragonRepository dohaeragonRepository;
    private final KelitisRepository kelitisRepository;

    @Transactional
    public Dohaeragon ejecutar(String dohaeragonId, String solicitanteId, String motivo) {
        Dohaeragon dohaeragon = dohaeragonRepository.obtener(dohaeragonId)
                .orElseThrow(() -> new NoSuchElementException("Dohaeragon no encontrada"));

        dohaeragon.validarParticipante(solicitanteId);
        dohaeragon.cancelar(motivo);

        kelitisRepository.obtenerPorDohaeragon(dohaeragonId).ifPresent(kelitis -> {
            kelitis.solicitarReembolso(motivo);
            kelitisRepository.guardar(kelitis);
        });

        dohaeragonRepository.guardar(dohaeragon);
        return dohaeragon;
    }
}
