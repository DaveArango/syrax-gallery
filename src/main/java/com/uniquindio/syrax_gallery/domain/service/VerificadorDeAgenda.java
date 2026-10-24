package com.uniquindio.syrax_gallery.domain.service;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;


public class VerificadorDeAgenda {

    private final SunfyreRepository sunfyreRepository;

    public VerificadorDeAgenda(SunfyreRepository sunfyreRepository) {
        this.sunfyreRepository = sunfyreRepository;
    }

    public void verificarDisponibilidad(String idAzantys, Sari sari, String idSunfyre) {
        boolean hayCruce = sunfyreRepository.obtenerAbonadasDeAzantys(idAzantys).stream()
                .filter(otra -> !otra.getId().equals(idSunfyre))
                .anyMatch(otra -> otra.getSari().seSolapaCon(sari));

        if (hayCruce) {
            throw new ReglaDominioException("El Azantys ya tiene una cita en ese horario");
        }
    }
}
