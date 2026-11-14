package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Gida;
import com.uniquindio.syrax_gallery.domain.repository.GidaRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.GidaPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GidaRepositoryJpa implements GidaRepository {

    private final GidaJpaDataRepository dataRepository;
    private final GidaPersistenceMapper mapper;

    @Override
    public void guardar(Gida gida) {
        dataRepository.save(mapper.toEntity(gida));
    }

    @Override
    public List<Gida> obtenerPorCaraxes(String idCaraxes) {
        return dataRepository.findByIdCaraxesOrderByFechaDesc(idCaraxes).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
