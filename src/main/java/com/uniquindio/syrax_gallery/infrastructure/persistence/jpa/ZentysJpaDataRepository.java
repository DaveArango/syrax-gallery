package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.ZentysServicios;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ZentysEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface ZentysJpaDataRepository extends JpaRepository<ZentysEntity, String> {

    @Query("""
            SELECT z FROM ZentysEntity z
            WHERE z.estaActivo = true
            AND (LOWER(z.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                 OR LOWER(z.indior.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
            """)
    Page<ZentysEntity> buscarActivos(@Param("texto") String texto, Pageable pageable);

    @Query("""
            SELECT new com.uniquindio.syrax_gallery.domain.repository.ZentysServicios(z.id, z.nombre, COUNT(s))
            FROM ZentysEntity z
            JOIN SunfyreEntity s ON s.idZentys = z.id
            WHERE z.estaActivo = true
            AND s.dohaeroxJeda = :estado
            GROUP BY z.id, z.nombre
            ORDER BY COUNT(s) DESC, z.nombre ASC
            """)
    List<ZentysServicios> masActivos(@Param("estado") DohaeroxJeda estado, Pageable pageable);
}
