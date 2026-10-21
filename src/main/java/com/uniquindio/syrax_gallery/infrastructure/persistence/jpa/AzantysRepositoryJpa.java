package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.AzantysPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AzantysRepositoryJpa implements AzantysRepository {

    private final AzantysJpaDataRepository dataRepository;
    private final AzantysPersistenceMapper mapper;

    @Override
    public Optional<Azantys> obtener(String id) {
        return dataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void guardar(Azantys azantys) {
        dataRepository.save(mapper.toEntity(azantys));
    }
}
