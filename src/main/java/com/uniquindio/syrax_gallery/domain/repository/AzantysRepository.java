package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;

import java.util.Optional;

public interface AzantysRepository {
    Optional<Azantys> obtener(String id);
    void guardar(Azantys azantys);
}
