package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CancelarSunfyreUseCase {

    private final SunfyreRepository sunfyreRepository;
    private final KelitisRepository kelitisRepository;

    @Transactional
    public Sunfyre ejecutar(String sunfyreId, String solicitanteId, String motivo) {
        Sunfyre sunfyre = sunfyreRepository.obtener(sunfyreId)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));

        sunfyre.validarParticipante(solicitanteId);
        sunfyre.cancelar(motivo);

        kelitisRepository.obtenerPorSunfyre(sunfyreId).ifPresent(kelitis -> {
            kelitis.solicitarReembolso(motivo);
            kelitisRepository.guardar(kelitis);
        });

        sunfyreRepository.guardar(sunfyre);
        return sunfyre;
    }
}
