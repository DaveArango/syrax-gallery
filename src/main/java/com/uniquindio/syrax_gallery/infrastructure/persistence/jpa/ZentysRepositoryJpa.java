package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysServicios;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ZentysEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.ZentysPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public Pagina<Zentys> buscarActivos(String texto, int pagina, int tamano) {
        String patron = (texto == null) ? "" : texto.trim();

        Page<ZentysEntity> resultado = dataRepository.buscarActivos(
                patron, PageRequest.of(pagina, tamano, Sort.by("nombre")));

        return new Pagina<>(
                resultado.getContent().stream().map(mapper::toDomain).toList(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.getNumber(),
                resultado.getSize());
    }

    @Override
    public List<ZentysServicios> masActivos(int limite) {
        return dataRepository.masActivos(DohaeroxJeda.COMPLETADO, PageRequest.of(0, limite));
    }
}
