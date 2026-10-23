package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.CaraxesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface CaraxesJpaDataRepository extends JpaRepository<CaraxesEntity, String> {
}
