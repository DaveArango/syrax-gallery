package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PublicarCaraxesUseCase {

    private final CaraxesRepository caraxesRepository;
    private final AzantysRepository azantysRepository;

    @Transactional
    public Caraxes ejecutar(String azantysId, String caraxesId, Kanez destino) {
        Azantys azantys = azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));
        Caraxes caraxes = caraxesRepository.obtener(caraxesId)
                .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"));

        caraxes.validarAutoria(azantys.getId());
        azantys.validarHabilitacionParaPublicar();
        caraxes.publicar(destino);

        caraxesRepository.guardar(caraxes);
        return caraxes;
    }
}