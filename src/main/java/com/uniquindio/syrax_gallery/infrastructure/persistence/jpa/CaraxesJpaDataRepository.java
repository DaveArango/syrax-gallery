package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.valueobject.CaraxesKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.CaraxesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

interface CaraxesJpaDataRepository extends JpaRepository<CaraxesEntity, String> {

    @Query("""
            SELECT c FROM CaraxesEntity c
            WHERE c.kanez IN (:publicadas)
            AND c.kastor IN (:kastores)
            AND (LOWER(c.sete.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                 OR LOWER(c.jorva.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
            """)
    Page<CaraxesEntity> buscarPublicadas(@Param("publicadas") Collection<Kanez> publicadas,
                                         @Param("kastores") Collection<CaraxesKastor> kastores,
                                         @Param("texto") String texto,
                                         Pageable pageable);
}
