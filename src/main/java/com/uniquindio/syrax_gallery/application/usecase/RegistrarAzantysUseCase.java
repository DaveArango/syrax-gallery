package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrarAzantysUseCase {

    private final AzantysRepository repository;

    @Transactional
    public Azantys ejecutar(String id, String nombre, Kostion kostion, Runiapos runiapos) {
        Azantys azantys = Azantys.crear(id, nombre, kostion, runiapos);
        repository.guardar(azantys);
        return azantys;
    }
}
