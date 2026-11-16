package com.uniquindio.syrax_gallery.domain.service;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;

import java.time.LocalDate;
import java.util.List;


public class VerificadorDeAgenda {

    private final DohaeragonRepository dohaeragonRepository;

    public VerificadorDeAgenda(DohaeragonRepository dohaeragonRepository) {
        this.dohaeragonRepository = dohaeragonRepository;
    }

    /**
     * Ventanas de esa Sunfyre que siguen libres ese día:
     * ni empiezan en el pasado ni se cruzan con otra cita del mismo Azantys.
     */
    public List<Sari> ventanasLibres(Sunfyre sunfyre, LocalDate fecha) {
        List<Dohaeragon> ocupadas = dohaeragonRepository.obtenerActivasDeAzantys(sunfyre.getIdAzantys());

        return sunfyre.ventanasDel(fecha).stream()
                .filter(ventana -> !ventana.iniciaEnElPasado())
                .filter(ventana -> ocupadas.stream().noneMatch(otra -> otra.getSari().seSolapaCon(ventana)))
                .toList();
    }

    public void verificarVentanaLibre(Sunfyre sunfyre, Sari sari) {
        boolean estaLibre = ventanasLibres(sunfyre, sari.inicio().toLocalDate()).contains(sari);
        if (!estaLibre) {
            throw new ReglaDominioException("Ese horario no está disponible para esta Sunfyre");
        }
    }

    public void verificarDisponibilidad(String idAzantys, Sari sari, String idDohaeragon) {
        boolean hayCruce = dohaeragonRepository.obtenerAbonadasDeAzantys(idAzantys).stream()
                .filter(otra -> !otra.getId().equals(idDohaeragon))
                .anyMatch(otra -> otra.getSari().seSolapaCon(sari));

        if (hayCruce) {
            throw new ReglaDominioException("El Azantys ya tiene una cita en ese horario");
        }
    }
}
