package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SunfyreEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.SunfyrePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SunfyreRepositoryJpa implements SunfyreRepository {

    private final SunfyreJpaDataRepository dataRepository;
    private final SunfyrePersistenceMapper mapper;

    @Override
    public Optional<Sunfyre> obtener(String id) {
        return dataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void guardar(Sunfyre sunfyre) {
        dataRepository.save(mapper.toEntity(sunfyre));
    }

    @Override
    public Pagina<Sunfyre> buscarActivos(String texto, SunfyreKastor kastor, String ciudad,
                                            Double precioMin, Double precioMax, int pagina, int tamano) {
        // Un filtro que no llega se convierte en "todos"
        Set<SunfyreKastor> kastores = (kastor == null) ? EnumSet.allOf(SunfyreKastor.class) : EnumSet.of(kastor);

        Page<SunfyreEntity> resultado = dataRepository.buscarActivos(
                kastores,
                (ciudad == null) ? "" : ciudad.trim(),
                (precioMin == null) ? 0 : precioMin,
                (precioMax == null) ? Double.MAX_VALUE : precioMax,
                (texto == null) ? "" : texto.trim(),
                PageRequest.of(pagina, tamano, Sort.by("sete.nombre")));

        return new Pagina<>(
                resultado.getContent().stream().map(mapper::toDomain).toList(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.getNumber(),
                resultado.getSize());
    }

    @Override
    public List<Sunfyre> obtenerDeAzantys(String idAzantys) {
        return dataRepository.findByIdAzantys(idAzantys).stream()
                .map(mapper::toDomain)
                .sorted(Comparator.comparing(d -> d.getSete().nombre()))
                .toList();
    }
}
