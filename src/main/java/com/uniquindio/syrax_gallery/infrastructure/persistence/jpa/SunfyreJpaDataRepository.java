package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.SunfyreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

interface SunfyreJpaDataRepository extends JpaRepository<SunfyreEntity, String> {

    @Query("""
            SELECT s FROM SunfyreEntity s
            WHERE s.kanez IN (:publicadas)
            AND s.dohaeroxJeda = :disponible
            AND s.kastor IN (:kastores)
            AND LOWER(s.alion.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))
            """)
    Page<SunfyreEntity> buscarPublicadas(@Param("publicadas") Collection<Kanez> publicadas,
                                         @Param("disponible") DohaeroxJeda disponible,
                                         @Param("kastores") Collection<SunfyreKastor> kastores,
                                         @Param("ciudad") String ciudad,
                                         Pageable pageable);

    List<SunfyreEntity> findByIdAzantysAndDohaeroxJeda(String idAzantys, DohaeroxJeda dohaeroxJeda);
}
