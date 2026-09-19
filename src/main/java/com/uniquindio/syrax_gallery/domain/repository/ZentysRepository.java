package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;

import java.util.Optional;

public interface ZentysRepository {
    Optional<Zentys> obtener(String id);
    void guardar(Zentys zentys);
}
