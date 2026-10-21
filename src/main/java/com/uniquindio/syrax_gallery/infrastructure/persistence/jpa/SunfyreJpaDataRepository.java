package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SunfyreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface SunfyreJpaDataRepository extends JpaRepository<SunfyreEntity, String> {
}
