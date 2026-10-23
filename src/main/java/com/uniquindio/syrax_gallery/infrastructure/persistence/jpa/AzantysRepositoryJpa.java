package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.AzantysServicios;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.AzantysEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.AzantysPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public Pagina<Azantys> buscarHabilitados(String texto, int pagina, int tamano) {
        String patron = (texto == null) ? "" : texto.trim();

        Page<AzantysEntity> resultado = dataRepository.buscarHabilitados(
                patron, PageRequest.of(pagina, tamano, Sort.by("nombre")));

        return new Pagina<>(
                resultado.getContent().stream().map(mapper::toDomain).toList(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.getNumber(),
                resultado.getSize());
    }

    @Override
    public List<AzantysServicios> masSolicitados(int limite) {
        return dataRepository.masSolicitados(DohaeroxJeda.COMPLETADO, PageRequest.of(0, limite));
    }
}
