package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReprogramarDohaeragonUseCase {

    private final DohaeragonRepository repository;
    private final VerificadorDeAgenda verificadorDeAgenda;

    @Transactional
    public Dohaeragon ejecutar(String azantysId, String dohaeragonId, Sari nuevoSari) {
        Dohaeragon dohaeragon = repository.obtener(dohaeragonId)
                .orElseThrow(() -> new NoSuchElementException("Dohaeragon no encontrada"));

        dohaeragon.validarAutoria(azantysId);
        verificadorDeAgenda.verificarDisponibilidad(dohaeragon.getIdAzantys(), nuevoSari, dohaeragon.getId());
        dohaeragon.reprogramar(nuevoSari);

        repository.guardar(dohaeragon);
        return dohaeragon;
    }
}
