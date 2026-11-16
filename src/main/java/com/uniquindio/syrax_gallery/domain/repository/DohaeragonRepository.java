package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;

import java.util.List;
import java.util.Optional;

public interface DohaeragonRepository {

    Optional<Dohaeragon> obtener(String id);

    void guardar(Dohaeragon dohaeragon);

    Pagina<Dohaeragon> buscarPublicadas(String ciudad, SunfyreKastor kastor,
                                     Double precioMin, Double precioMax, int pagina, int tamano);

    Pagina<Dohaeragon> buscarDeAzantys(String idAzantys, DohaeroxJeda estado, int pagina, int tamano);

    List<Dohaeragon> obtenerAbonadasDeAzantys(String idAzantys);

    // Citas que ocupan la agenda del Azantys: PENDIENTE, ABONADO y EN_EJECUCION
    List<Dohaeragon> obtenerActivasDeAzantys(String idAzantys);

    // Las reservas de un Zentys, de la más próxima a la más lejana
    List<Dohaeragon> obtenerDeZentys(String idZentys);
}
