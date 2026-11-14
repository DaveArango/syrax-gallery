package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConfirmarSindesUseCase {

    private final SindesRepository sindesRepository;
    private final CaraxesRepository caraxesRepository;

    @Transactional
    public Sindes ejecutar(String sindesId, String azantysId) {
        Sindes sindes = sindesRepository.obtener(sindesId)
                .orElseThrow(() -> new NoSuchElementException("Sindes no encontrado"));
        sindes.validarAzantys(azantysId);

        Caraxes caraxes = caraxesRepository.obtener(sindes.getIdCaraxes())
                .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"));

        // Se confirma la compra y la obra queda VENDIDA: si una de las dos falla, no se guarda nada
        sindes.confirmar();
        caraxes.marcarComoVendido();

        sindesRepository.guardar(sindes);
        caraxesRepository.guardar(caraxes);
        return sindes;
    }
}