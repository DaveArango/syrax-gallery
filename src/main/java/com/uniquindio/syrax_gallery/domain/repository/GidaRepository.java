package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Gida;

import java.util.List;

public interface GidaRepository {

    void guardar(Gida gida);

    List<Gida> obtenerPorCaraxes(String idCaraxes);
}