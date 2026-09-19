package com.uniquindio.syrax_gallery.infrastructure;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegistrarZentysEnMemoria implements ZentysRepository {
    private final Map<String, Zentys> zentysMap = new HashMap<>();

    @Override
    public Optional<Zentys> obtener(String id) {
        return Optional.ofNullable(zentysMap.get(id));
    }

    @Override
    public void guardar(Zentys zentys) {
        zentysMap.put(zentys.getId(), zentys);
    }
}
