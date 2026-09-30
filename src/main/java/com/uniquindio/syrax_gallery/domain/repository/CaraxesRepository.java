package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;

import java.util.Optional;

public interface CaraxesRepository {
    Optional<Caraxes> obtener(String id);
    void guardar(Caraxes caraxes);
}
