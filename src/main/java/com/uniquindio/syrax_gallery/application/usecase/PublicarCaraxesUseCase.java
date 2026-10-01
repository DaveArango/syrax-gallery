package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import org.springframework.stereotype.Service;

@Service
public class PublicarCaraxesUseCase {
    private final CaraxesRepository caraxesRepository;
    private final AzantysRepository azantysRepository;

    public PublicarCaraxesUseCase(CaraxesRepository caraxesRepository,
                                  AzantysRepository azantysRepository) {
        this.caraxesRepository = caraxesRepository;
        this.azantysRepository = azantysRepository;
    }

    public Caraxes ejecutar(String id, Kanez destino) {
        Caraxes caraxes = caraxesRepository.obtener(id)
                .orElseThrow( () -> new IllegalArgumentException("Caraxes no encontrado"));
        Azantys azantys = azantysRepository.obtener(caraxes.getIdAzantys())
                .orElseThrow( () -> new IllegalArgumentException("Azantys no encontrado"));

        azantys.validarHabilitacionParaPublicar();
        caraxes.publicar(destino);

        caraxesRepository.guardar(caraxes);
        return caraxes;
    }
}
