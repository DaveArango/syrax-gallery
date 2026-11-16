package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CrearDohaeragonUseCase {

    private final DohaeragonRepository dohaeragonRepository;
    private final AzantysRepository azantysRepository;
    private final ZentysRepository zentysRepository;

    @Transactional
    public Dohaeragon ejecutar(String id, String azantysId, String zentysId, Sari sari,
                            SunfyreKastor kastor, Alion alion, Vala valaCongelado) {
        azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));
        zentysRepository.obtener(zentysId)
                .orElseThrow(() -> new NoSuchElementException("Zentys no encontrado"));

        Dohaeragon dohaeragon = Dohaeragon.solicitar(id, azantysId, zentysId,
                sari, kastor, alion, valaCongelado);
        dohaeragonRepository.guardar(dohaeragon);
        return dohaeragon;
    }
}
