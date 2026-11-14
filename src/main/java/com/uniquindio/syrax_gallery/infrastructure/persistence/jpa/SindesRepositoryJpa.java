package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.SindesJeda;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.SindesPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SindesRepositoryJpa implements SindesRepository {

    private final SindesJpaDataRepository dataRepository;
    private final SindesPersistenceMapper mapper;

    @Override
    public Optional<Sindes> obtener(String id) {
        return dataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void guardar(Sindes sindes) {
        dataRepository.save(mapper.toEntity(sindes));
    }

    @Override
    public List<Sindes> obtenerPorZentys(String idZentys) {
        return dataRepository.findByIdZentysOrderByFechaDesc(idZentys).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePendientePorCaraxes(String idCaraxes) {
        return dataRepository.existsByIdCaraxesAndSindesJeda(idCaraxes, SindesJeda.PENDIENTE);
    }

    @Override
    public boolean existeCompletada(String idZentys, String idCaraxes) {
        return dataRepository.existsByIdZentysAndIdCaraxesAndSindesJeda(
                idZentys, idCaraxes, SindesJeda.COMPLETADA);
    }
}
