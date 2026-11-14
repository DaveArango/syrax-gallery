package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.valueobject.SindesJeda;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SindesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SindesJpaDataRepository extends JpaRepository<SindesEntity, String> {

    List<SindesEntity> findByIdZentysOrderByFechaDesc(String idZentys);

    boolean existsByIdCaraxesAndSindesJeda(String idCaraxes, SindesJeda sindesJeda);

    boolean existsByIdZentysAndIdCaraxesAndSindesJeda(String idZentys, String idCaraxes, SindesJeda sindesJeda);
}