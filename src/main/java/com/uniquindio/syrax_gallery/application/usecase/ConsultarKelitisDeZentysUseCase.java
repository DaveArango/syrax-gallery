package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarKelitisDeZentysUseCase {

    private final KelitisRepository repository;

    // Los pagos (Kelitis) del Zentys: de aquí sale el id para aprobar el servicio
    @Transactional(readOnly = true)
    public List<Kelitis> ejecutar(String zentysId) {
        return repository.obtenerDeZentys(zentysId);
    }
}
