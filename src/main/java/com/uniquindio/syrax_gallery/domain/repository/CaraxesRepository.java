package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.valueobject.CaraxesKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;

import java.util.List;
import java.util.Optional;

public interface CaraxesRepository {
    Optional<Caraxes> obtener(String id);
    void guardar(Caraxes caraxes);
    Pagina<Caraxes> buscarPublicadas(String texto, CaraxesKastor kastor, int pagina, int tamano);
}
