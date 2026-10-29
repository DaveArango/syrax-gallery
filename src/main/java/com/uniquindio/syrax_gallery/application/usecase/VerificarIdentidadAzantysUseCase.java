package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;
import com.uniquindio.syrax_gallery.domain.valueobject.Laehurlion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VerificarIdentidadAzantysUseCase {

    private final AzantysRepository repository;

    @Transactional
    public Azantys ejecutar(String id, String solicitanteId, Iksia iksia, Laehurlion laehurlion) {
        Azantys azantys = repository.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));
        azantys.validarTitular(solicitanteId);
        azantys.verificarIksia(iksia);
        azantys.subirLaehurlion(laehurlion);
        repository.guardar(azantys);
        return azantys;
    }
}
