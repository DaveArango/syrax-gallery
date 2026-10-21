package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.AzantysEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface AzantysJpaDataRepository extends JpaRepository<AzantysEntity, String> {
}
