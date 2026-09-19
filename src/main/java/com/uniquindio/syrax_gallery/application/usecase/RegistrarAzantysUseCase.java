package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;

public class RegistrarAzantysUseCase{
    private final AzantysRepository repository;

    public RegistrarAzantysUseCase(AzantysRepository repository) {
        this.repository = repository;
    }

    public Azantys ejecutar(String id, String nombre, Kostion kostion, Runiapos runiapos, String lentorId){
        Azantys azantys = Azantys.crear(id, nombre, kostion, runiapos, lentorId);
        repository.guardar(azantys);
        return azantys;
    }
}
