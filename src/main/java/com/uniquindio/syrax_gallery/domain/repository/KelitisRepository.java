package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;

import java.util.Optional;

public interface KelitisRepository {

    Optional<Kelitis> obtener(String id);

    void guardar(Kelitis kelitis);

    boolean existePorSunfyre(String idSunfyre);
}
