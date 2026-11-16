package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConsultarDisponibilidadSunfyreUseCase {

    private final SunfyreRepository sunfyreRepository;
    private final VerificadorDeAgenda verificadorDeAgenda;

    // Las horas libres de una Sunfyre en una fecha, para que el Zentys escoja una
    @Transactional(readOnly = true)
    public List<Sari> ejecutar(String sunfyreId, LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        Sunfyre sunfyre = sunfyreRepository.obtener(sunfyreId)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));

        if (!sunfyre.isActivo()) {
            return List.of();
        }
        return verificadorDeAgenda.ventanasLibres(sunfyre, fecha);
    }
}
