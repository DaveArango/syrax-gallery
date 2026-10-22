package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.CaraxesPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CaraxesRepositoryJpa implements CaraxesRepository {

    private final CaraxesJpaDataRepository dataRepository;
    private final CaraxesPersistenceMapper mapper;


    @Override
    public Optional<Caraxes> obtener(String id) {
        return dataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void guardar(Caraxes caraxes) {
        dataRepository.save(mapper.toEntity(caraxes));
    }
}
