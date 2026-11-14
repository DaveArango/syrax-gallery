package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.GidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface GidaJpaDataRepository extends JpaRepository<GidaEntity, String> {

    List<GidaEntity> findByIdCaraxesOrderByFechaDesc(String idCaraxes);
}