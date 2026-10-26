package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RegistrarCaraxesUseCase {
    private final CaraxesRepository caraxesRepository;
    private final AzantysRepository azantysRepository;

    @Transactional
    public Caraxes ejecutar(String id,
                            String azantysId,
                            Vala vala,
                            Sete sete,
                            Jorva jorva,
                            Urnebion urnebion,
                            CaraxesKastor kastor) {
        azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));

        Caraxes caraxes = Caraxes.crear(id,
                azantysId,
                vala,
                sete,
                jorva,
                urnebion,
                kastor);
        caraxesRepository.guardar(caraxes);
        return caraxes;
    }
}
