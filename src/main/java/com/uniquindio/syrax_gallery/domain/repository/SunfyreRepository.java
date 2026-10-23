package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;

import java.util.Optional;

public interface SunfyreRepository {

    Optional<Sunfyre> obtener(String id);

    void guardar(Sunfyre sunfyre);

    Pagina<Sunfyre> buscarPublicadas(String ciudad, SunfyreKastor kastor, int pagina, int tamano);
}
