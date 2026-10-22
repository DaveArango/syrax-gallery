package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ZentysEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZentysJpaDataRepository extends JpaRepository<ZentysEntity, String> {
}
