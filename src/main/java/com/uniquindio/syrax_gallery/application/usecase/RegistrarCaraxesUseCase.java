package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;

public class RegistrarCaraxesUseCase {
    private final CaraxesRepository caraxesRepository;
    private final AzantysRepository azantysRepository;

    public RegistrarCaraxesUseCase(CaraxesRepository caraxesRepository,
                                   AzantysRepository azantysRepository) {
        this.caraxesRepository = caraxesRepository;
        this.azantysRepository = azantysRepository;
    }

    public Caraxes ejecutar(String id,
                            String azantysId,
                            Vala vala,
                            Sete sete,
                            Jorva jorva,
                            Urnebion urnebion,
                            CaraxesKastor kastor) {
        azantysRepository.obtener(azantysId)
                .orElseThrow(() -> new IllegalArgumentException("Azantys no encontrado"));

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
