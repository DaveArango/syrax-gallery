package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SunfyreEntity;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.SunfyrePersistenceMapper;
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
public class SunfyreRepositoryJpa implements SunfyreRepository {

    private static final Set<Kanez> PUBLICADAS = EnumSet.of(Kanez.EXHIBICION, Kanez.EN_VENTA);

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

    @Override
    public Pagina<Sunfyre> buscarPublicadas(String ciudad, SunfyreKastor kastor,
                                            Double precioMin, Double precioMax, int pagina, int tamano) {
        String patron = (ciudad == null) ? "" : ciudad.trim();
        Set<SunfyreKastor> kastores = (kastor == null) ? EnumSet.allOf(SunfyreKastor.class) : EnumSet.of(kastor);
        double minimo = (precioMin == null) ? 0 : precioMin;
        double maximo = (precioMax == null) ? Double.MAX_VALUE : precioMax;

        Page<SunfyreEntity> resultado = dataRepository.buscarPublicadas(
                PUBLICADAS, DohaeroxJeda.PENDIENTE, kastores, patron, minimo, maximo,
                PageRequest.of(pagina, tamano, Sort.by("sariInicio")));
        return new Pagina<>(
                resultado.getContent().stream().map(mapper::toDomain).toList(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.getNumber(),
                resultado.getSize());
    }

    @Override
    public List<Sunfyre> obtenerAbonadasDeAzantys(String idAzantys) {
        return dataRepository.findByIdAzantysAndDohaeroxJeda(idAzantys, DohaeroxJeda.ABONADO).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Pagina<Sunfyre> buscarDeAzantys(String idAzantys, DohaeroxJeda estado, int pagina, int tamano) {
        Page<SunfyreEntity> resultado = dataRepository.buscarDeAzantys(
                idAzantys, estados(estado),
                PageRequest.of(pagina, tamano, Sort.by("sariInicio").descending())); // más reciente primero
        return aPagina(resultado);
    }

    private Set<DohaeroxJeda> estados(DohaeroxJeda estado) {
        return (estado == null) ? EnumSet.allOf(DohaeroxJeda.class) : EnumSet.of(estado);
    }

    private Pagina<Sunfyre> aPagina(Page<SunfyreEntity> resultado) {
        return new Pagina<>(
                resultado.getContent().stream().map(mapper::toDomain).toList(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.getNumber(),
                resultado.getSize());
    }
}