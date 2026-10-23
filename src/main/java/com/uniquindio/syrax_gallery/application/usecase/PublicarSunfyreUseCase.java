package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PublicarSunfyreUseCase {

    private final SunfyreRepository sunfyreRepository;
    private final AzantysRepository azantysRepository;

    @Transactional
    public Sunfyre ejecutar(String azantysId, String sunfyreId, Kanez destino) {
        Azantys azantys = azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));
        Sunfyre sunfyre = sunfyreRepository.obtener(sunfyreId)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));

        sunfyre.validarAutoria(azantys.getId());
        azantys.validarHabilitacionParaPublicar();
        sunfyre.publicar(destino);

        sunfyreRepository.guardar(sunfyre);
        return sunfyre;
    }
}
