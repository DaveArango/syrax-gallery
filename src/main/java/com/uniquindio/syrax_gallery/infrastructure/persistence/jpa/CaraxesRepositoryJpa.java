package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.CaraxesKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.CaraxesEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.CaraxesPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
@RequiredArgsConstructor
public class CaraxesRepositoryJpa implements CaraxesRepository {

    private static final Set<Kanez> PUBLICADAS = EnumSet.of(Kanez.EXHIBICION, Kanez.EN_VENTA);

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

    @Override
    public Pagina<Caraxes> buscarPublicadas(String texto, CaraxesKastor kastor, int pagina, int tamano) {
        String patron = (texto == null) ? "" : texto.trim();
        Set<CaraxesKastor> kastores = (kastor == null) ? EnumSet.allOf(CaraxesKastor.class) : EnumSet.of(kastor);

        Page<CaraxesEntity> resultado = dataRepository.buscarPublicadas(
                PUBLICADAS,
                kastores,
                patron,
                PageRequest.of(pagina, tamano, Sort.by("sete.nombre")));

        return new Pagina<>(
                resultado.getContent().stream().map(mapper::toDomain).toList(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.getNumber(),
                resultado.getSize());
    }
}
