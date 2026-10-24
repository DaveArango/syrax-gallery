package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReprogramarSunfyreUseCase {

    private final SunfyreRepository repository;
    private final VerificadorDeAgenda verificadorDeAgenda;

    @Transactional
    public Sunfyre ejecutar(String azantysId, String sunfyreId, Sari nuevoSari) {
        Sunfyre sunfyre = repository.obtener(sunfyreId)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));

        sunfyre.validarAutoria(azantysId);
        verificadorDeAgenda.verificarDisponibilidad(sunfyre.getIdAzantys(), nuevoSari, sunfyre.getId());
        sunfyre.reprogramar(nuevoSari);

        repository.guardar(sunfyre);
        return sunfyre;
    }
}
