package com.uniquindio.syrax_gallery.infrastructure.persistence;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
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
