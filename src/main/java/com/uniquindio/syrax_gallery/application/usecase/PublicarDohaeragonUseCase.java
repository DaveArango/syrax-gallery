package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PublicarDohaeragonUseCase {

    private final DohaeragonRepository dohaeragonRepository;
    private final AzantysRepository azantysRepository;

    @Transactional
    public Dohaeragon ejecutar(String azantysId, String dohaeragonId, Kanez destino) {
        Azantys azantys = azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));
        Dohaeragon dohaeragon = dohaeragonRepository.obtener(dohaeragonId)
                .orElseThrow(() -> new NoSuchElementException("Dohaeragon no encontrada"));

        dohaeragon.validarAutoria(azantys.getId());
        azantys.validarHabilitacionParaPublicar();
        dohaeragon.publicar(destino);

        dohaeragonRepository.guardar(dohaeragon);
        return dohaeragon;
    }
}
