package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Sindes;

import java.util.List;
import java.util.Optional;

public interface SindesRepository {

    Optional<Sindes> obtener(String id);

    void guardar(Sindes sindes);

    List<Sindes> obtenerPorZentys(String idZentys);

    boolean existePendientePorCaraxes(String idCaraxes);

    boolean existeCompletada(String idZentys, String idCaraxes);

    // Las ventas de un Azantys, de la más reciente a la más antigua
    List<Sindes> obtenerPorAzantys(String idAzantys);
}
