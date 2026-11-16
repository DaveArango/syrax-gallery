package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.KelitisPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class KelitisRepositoryJpa implements KelitisRepository {

    private final KelitisJpaDataRepository dataRepository;
    private final KelitisPersistenceMapper mapper;

    @Override
    public Optional<Kelitis> obtener(String id) {
        return dataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void guardar(Kelitis kelitis) {
        dataRepository.save(mapper.toEntity(kelitis));
    }

    @Override
    public boolean existePorDohaeragon(String idDohaeragon) {
        return dataRepository.existsByIdDohaeragon(idDohaeragon);
    }

    @Override
    public Optional<Kelitis> obtenerPorDohaeragon(String idDohaeragon) {
        return dataRepository.findByIdDohaeragon(idDohaeragon).map(mapper::toDomain);
    }
}
