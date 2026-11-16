package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;

import java.util.List;
import java.util.Optional;

public interface KelitisRepository {

    Optional<Kelitis> obtener(String id);

    void guardar(Kelitis kelitis);

    boolean existePorDohaeragon(String idDohaeragon);

    Optional<Kelitis> obtenerPorDohaeragon(String idDohaeragon);

    // Los pagos de un Zentys, para que pueda aprobarlos
    List<Kelitis> obtenerDeZentys(String idZentys);
}
