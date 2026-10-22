package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.ZentysPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ZentysRepositoryJpa implements ZentysRepository {

    private final ZentysJpaDataRepository dataRepository;
    private final ZentysPersistenceMapper mapper;


    @Override
    public Optional<Zentys> obtener(String id) {
        return dataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void guardar(Zentys zentys) {
        dataRepository.save(mapper.toEntity(zentys));
    }
}
