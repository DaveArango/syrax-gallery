package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Gida;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.GidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConsultarGidaDeCaraxesUseCase {

    private final GidaRepository gidaRepository;
    private final CaraxesRepository caraxesRepository;

    @Transactional(readOnly = true)
    public List<Gida> ejecutar(String caraxesId) {
        caraxesRepository.obtener(caraxesId)
                .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"));
        return gidaRepository.obtenerPorCaraxes(caraxesId);
    }
}
