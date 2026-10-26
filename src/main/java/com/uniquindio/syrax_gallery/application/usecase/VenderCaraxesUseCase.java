package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VenderCaraxesUseCase {

    private final CaraxesRepository caraxesRepository;
    private final ZentysRepository zentysRepository;

    @Transactional
    public Caraxes ejecutar(String zentysId, String caraxesId) {
        Zentys zentys = zentysRepository.obtener(zentysId)
                .orElseThrow(() -> new NoSuchElementException("Zentys no encontrado"));
        Caraxes caraxes = caraxesRepository.obtener(caraxesId)
                .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"));

        zentys.validarPuedeComprar();
        caraxes.marcarComoVendido();

        caraxesRepository.guardar(caraxes);
        return caraxes;
    }
}
