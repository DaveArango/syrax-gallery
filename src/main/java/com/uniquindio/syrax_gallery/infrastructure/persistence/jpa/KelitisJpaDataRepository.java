package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.KelitisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface KelitisJpaDataRepository extends JpaRepository<KelitisEntity, String> {

    boolean existsByIdDohaeragon(String idDohaeragon);

    Optional<KelitisEntity> findByIdDohaeragon(String idDohaeragon);
}
