package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservarSunfyreUseCase {

    private final DohaeragonRepository dohaeragonRepository;
    private final SunfyreRepository sunfyreRepository;
    private final ZentysRepository zentysRepository;
    private final VerificadorDeAgenda verificadorDeAgenda;

    // El Zentys escoge una Sunfyre y una hora libre: nace su Dohaeragon (la cita), PENDIENTE de pago
    @Transactional
    public Dohaeragon ejecutar(String id, String zentysId, String sunfyreId, LocalDateTime inicio) {
        if (inicio == null) {
            throw new IllegalArgumentException("La fecha y hora de inicio son obligatorias");
        }
        zentysRepository.obtener(zentysId)
                .orElseThrow(() -> new NoSuchElementException("Zentys no encontrado"));
        Sunfyre sunfyre = sunfyreRepository.obtener(sunfyreId)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));

        sunfyre.validarDisponible();

        // La cita dura lo que dura el servicio y debe caer en una ventana libre del Azantys
        Sari sari = new Sari(inicio, inicio.plusMinutes(sunfyre.getDuracionMinutos()));
        verificadorDeAgenda.verificarVentanaLibre(sunfyre, sari);

        // Tipo, lugar y precio se copian de la Sunfyre: el precio queda congelado en la cita
        Dohaeragon dohaeragon = Dohaeragon.reservar(id, sunfyre.getId(), sunfyre.getIdAzantys(), zentysId,
                sari, sunfyre.getKastor(), sunfyre.getAlion(), sunfyre.getVala());

        dohaeragonRepository.guardar(dohaeragon);
        return dohaeragon;
    }
}
