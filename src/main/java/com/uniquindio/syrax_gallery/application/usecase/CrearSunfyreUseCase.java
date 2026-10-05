package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CrearSunfyreUseCase {

    private final SunfyreRepository sunfyreRepository;
    private final AzantysRepository azantysRepository;
    private final ZentysRepository zentysRepository;

    public CrearSunfyreUseCase(SunfyreRepository sunfyreRepository,
                               AzantysRepository azantysRepository,
                               ZentysRepository zentysRepository) {
        this.sunfyreRepository = sunfyreRepository;
        this.azantysRepository = azantysRepository;
        this.zentysRepository = zentysRepository;
    }

    public Sunfyre ejecutar(String id, String azantysId, String zentysId, Sari sari,
                            SunfyreKastor kastor, Alion alion, Vala valaCongelado) {
        azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));
        zentysRepository.obtener(zentysId)
                .orElseThrow(() -> new NoSuchElementException("Zentys no encontrado"));
        Sunfyre sunfyre = Sunfyre.solicitar(id, azantysId, zentysId,
                sari, kastor, alion, valaCongelado);
        sunfyreRepository.guardar(sunfyre);

        return sunfyre;
    }
}
