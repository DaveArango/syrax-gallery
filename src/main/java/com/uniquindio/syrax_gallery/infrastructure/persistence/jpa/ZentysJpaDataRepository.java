package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ZentysEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface ZentysJpaDataRepository extends JpaRepository<ZentysEntity, String> {

    List<ZentysEntity> findByEstaActivoTrueOrderByNombreAsc();
}
