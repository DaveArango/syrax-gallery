package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * CU-38: el Zentys aprueba la ejecución del servicio y se libera el pago al Azantys.
 * Solo aplica cuando el Azantys ya marcó la Sunfyre como COMPLETADO.
 */
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
        if (sunfyre.getDohaeroxJeda() != DohaeroxJeda.COMPLETADO) {
            throw new ReglaDominioException("Solo se puede aprobar un servicio ya ejecutado");
        }

        kelitis.liberarPago(); // RETENIDO -> LIBERADO
        kelitisRepository.guardar(kelitis);
        return kelitis;
    }
}
