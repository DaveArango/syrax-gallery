package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

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
            SELECT d FROM SunfyreEntity d
            WHERE d.activo = true
            AND d.kastor IN (:kastores)
            AND LOWER(d.alion.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))
            AND d.vala.monto BETWEEN :precioMin AND :precioMax
            AND (LOWER(d.sete.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                 OR LOWER(d.jorva.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
            """)
    Page<SunfyreEntity> buscarActivos(@Param("kastores") Collection<SunfyreKastor> kastores,
                                         @Param("ciudad") String ciudad,
                                         @Param("precioMin") double precioMin,
                                         @Param("precioMax") double precioMax,
                                         @Param("texto") String texto,
                                         Pageable pageable);

    List<SunfyreEntity> findByIdAzantys(String idAzantys);
}
