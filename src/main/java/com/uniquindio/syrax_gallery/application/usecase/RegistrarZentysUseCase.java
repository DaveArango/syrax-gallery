package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.Indior;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;

public class RegistrarZentysUseCase {
    private final ZentysRepository repository;

    public RegistrarZentysUseCase(ZentysRepository repository) {
        this.repository = repository;
    }

    public Zentys ejecutar(String id,
                           String nombre,
                           Runiapos runiapos,
                           Indior indior){
        Zentys zentys = Zentys.crear(id,
                nombre,
                runiapos,
                indior);
        repository.guardar(zentys);
        return zentys;
    }
}
