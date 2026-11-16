package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Jedar;
import com.uniquindio.syrax_gallery.domain.valueobject.Jorva;
import com.uniquindio.syrax_gallery.domain.valueobject.Sete;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Urnebion;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CrearSunfyreUseCase {

    private final SunfyreRepository sunfyreRepository;
    private final AzantysRepository azantysRepository;

    @Transactional
    public Sunfyre ejecutar(String id, String azantysId, Sete sete, Jorva jorva, Urnebion urnebion,
                               SunfyreKastor kastor, Alion alion, Vala vala, int duracionMinutos,
                               Jedar horario) {
        azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));

        Sunfyre sunfyre = Sunfyre.crear(id, azantysId, sete, jorva, urnebion, kastor,
                alion, vala, duracionMinutos, horario);
        sunfyreRepository.guardar(sunfyre);
        return sunfyre;
    }
}
