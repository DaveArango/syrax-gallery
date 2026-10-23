package com.uniquindio.syrax_gallery.infrastructure.persistence;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.AzantysServicios;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@Repository
public class RegistrarSunfyreEnMemoria implements SunfyreRepository {

    private final Map<String, Sunfyre> sunfyreMap = new HashMap<>();

    @Override
    public Optional<Sunfyre> obtener(String id) {
        return Optional.ofNullable(sunfyreMap.get(id));
    }

    @Override
    public void guardar(Sunfyre sunfyre) {
        sunfyreMap.put(sunfyre.getId(), sunfyre);
    }

    @Override
    public Pagina<Sunfyre> buscarPublicadas(String ciudad, SunfyreKastor kastor, int pagina, int tamano) {
        throw new UnsupportedOperationException("Solo disponible con JPA");
    }

}


