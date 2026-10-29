package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class ConstituirKelitisUseCase {

    private final KelitisRepository kelitisRepository;
    private final SunfyreRepository sunfyreRepository;
    private final VerificadorDeAgenda verificadorDeAgenda;

    @Transactional
    public Kelitis ejecutar(String id, String sunfyreId, String zentysId) {
        Sunfyre sunfyre = sunfyreRepository.obtener(sunfyreId)
                .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"));
        sunfyre.validarCliente(zentysId);
        if (kelitisRepository.existePorSunfyre(sunfyreId)) {
            throw new ReglaDominioException("La Sunfyre ya tiene un Kelitis constituido");
        }

        verificadorDeAgenda.verificarDisponibilidad(sunfyre.getIdAzantys(), sunfyre.getSari(), sunfyre.getId());

        sunfyre.confirmar();
        Kelitis kelitis = Kelitis.realizar(id, sunfyre.getId(), sunfyre.getIdAzantys(),
                sunfyre.getIdZentys(), sunfyre.getValaCongelado());

        sunfyreRepository.guardar(sunfyre);
        kelitisRepository.guardar(kelitis);
        return kelitis;
    }
}
