package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;

import java.util.List;
import java.util.Optional;

public interface SunfyreRepository {

    Optional<Sunfyre> obtener(String id);

    void guardar(Sunfyre sunfyre);

    Pagina<Sunfyre> buscarActivos(String texto, SunfyreKastor kastor, String ciudad,
                                     Double precioMin, Double precioMax, int pagina, int tamano);

    List<Sunfyre> obtenerDeAzantys(String idAzantys);
}
