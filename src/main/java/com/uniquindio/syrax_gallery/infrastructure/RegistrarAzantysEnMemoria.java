package com.uniquindio.syrax_gallery.infrastructure;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegistrarAzantysEnMemoria implements AzantysRepository {
    private final Map<String, Azantys> azantyr = new HashMap<>();

    @Override
    public Optional<Azantys> obtener(String id) {
        return Optional.ofNullable(azantyr.get(id));
    }

    @Override
    public void guardar(Azantys azantys) {
        azantyr.put(azantys.getId(), azantys);
    }
}
