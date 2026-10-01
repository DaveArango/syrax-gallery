package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;

import java.util.Optional;

public interface SunfyreRepository {
    Optional<Sunfyre> obtener(String id);
    void guardar(Sunfyre sunfyre);
}
