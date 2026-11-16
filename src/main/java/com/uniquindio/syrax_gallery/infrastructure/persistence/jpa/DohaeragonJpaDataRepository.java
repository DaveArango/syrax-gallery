package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.DohaeragonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

interface DohaeragonJpaDataRepository extends JpaRepository<DohaeragonEntity, String> {

    @Query("""
            SELECT s FROM DohaeragonEntity s
            WHERE s.kanez IN (:publicadas)
            AND s.dohaeroxJeda = :disponible
            AND s.kastor IN (:kastores)
            AND LOWER(s.alion.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))
            AND s.valaCongelado.monto BETWEEN :precioMin AND :precioMax
            """)
    Page<DohaeragonEntity> buscarPublicadas(@Param("publicadas") Collection<Kanez> publicadas,
                                         @Param("disponible") DohaeroxJeda disponible,
                                         @Param("kastores") Collection<SunfyreKastor> kastores,
                                         @Param("ciudad") String ciudad,
                                         @Param("precioMin") double precioMin,
                                         @Param("precioMax") double precioMax,
                                         Pageable pageable);

    @Query("""
            SELECT s FROM DohaeragonEntity s
            WHERE s.idAzantys = :idAzantys
            AND s.dohaeroxJeda IN (:estados)
            """)
    Page<DohaeragonEntity> buscarDeAzantys(@Param("idAzantys") String idAzantys,
                                        @Param("estados") Collection<DohaeroxJeda> estados,
                                        Pageable pageable);

    List<DohaeragonEntity> findByIdAzantysAndDohaeroxJeda(String idAzantys, DohaeroxJeda dohaeroxJeda);

    List<DohaeragonEntity> findByIdAzantysAndDohaeroxJedaIn(String idAzantys, Collection<DohaeroxJeda> estados);

    List<DohaeragonEntity> findByIdZentysOrderBySariInicioAsc(String idZentys);

}
