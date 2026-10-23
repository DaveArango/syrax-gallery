package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.repository.AzantysServicios;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.AzantysEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface AzantysJpaDataRepository extends JpaRepository<AzantysEntity, String> {

    @Query("""
            SELECT a FROM AzantysEntity a
            WHERE a.activo = true
            AND a.iksiaVerificada = true
            AND a.laehurlionVerificado = true
            AND (LOWER(a.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                 OR LOWER(a.kostion) LIKE LOWER(CONCAT('%', :texto, '%')))
            """)
    Page<AzantysEntity> buscarHabilitados(@Param("texto") String texto, Pageable pageable);

    @Query("""
            SELECT new com.uniquindio.syrax_gallery.domain.repository.AzantysServicios(a.id, a.nombre, COUNT(s))
            FROM AzantysEntity a
            JOIN SunfyreEntity s ON s.idAzantys = a.id
            WHERE s.dohaeroxJeda = :estado
            GROUP BY a.id, a.nombre
            ORDER BY COUNT(s) DESC, a.nombre ASC
            """)
    List<AzantysServicios> masSolicitados(@Param("estado") DohaeroxJeda estado, Pageable pageable);
}