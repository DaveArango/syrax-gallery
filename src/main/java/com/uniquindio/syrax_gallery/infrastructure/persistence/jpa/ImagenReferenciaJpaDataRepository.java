package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.valueobject.TipoImagenReferencia;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ImagenReferenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ImagenReferenciaJpaDataRepository extends JpaRepository<ImagenReferenciaEntity, Long> {

    List<ImagenReferenciaEntity> findByTipoAndIdPropietarioOrderByOrdenAsc(TipoImagenReferencia tipo,
                                                                           String idPropietario);

    void deleteByTipoAndIdPropietario(TipoImagenReferencia tipo, String idPropietario);
}