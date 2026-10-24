package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RevisarVerificacionAzantysUseCase {

    private final AzantysRepository repository;

    @Transactional
    public Azantys ejecutar(String id, boolean aprobada, String motivo) {
        Azantys azantys = repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));

        if (aprobada) {
            azantys.aprobarIdentidad();
        } else {
            azantys.rechazarIdentidad(motivo);
        }

        repository.guardar(azantys);
        return azantys;
    }
}
