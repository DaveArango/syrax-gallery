package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.SunfyrePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}