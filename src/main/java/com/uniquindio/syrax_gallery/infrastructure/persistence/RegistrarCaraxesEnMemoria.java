package com.uniquindio.syrax_gallery.infrastructure.persistence;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RegistrarCaraxesEnMemoria implements CaraxesRepository {
    private final Map<String, Caraxes> caraxesMap = new HashMap<>();

    @Override
    public Optional<Caraxes> obtener(String id) {
        return Optional.ofNullable(caraxesMap.get(id));
    }

    @Override
    public void guardar(Caraxes caraxes) {
        caraxesMap.put(caraxes.getId(), caraxes);
    }
}
