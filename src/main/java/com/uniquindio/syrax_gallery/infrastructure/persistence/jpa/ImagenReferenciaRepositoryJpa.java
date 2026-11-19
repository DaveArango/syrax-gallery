package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.repository.ImagenReferenciaRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.TipoImagenReferencia;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity.ImagenReferenciaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImagenReferenciaRepositoryJpa implements ImagenReferenciaRepository {

    private final ImagenReferenciaJpaDataRepository dataRepository;

    @Override
    public List<String> listar(TipoImagenReferencia tipo, String propietarioId) {
        return dataRepository.findByTipoAndIdPropietarioOrderByOrdenAsc(tipo, propietarioId).stream()
                .map(ImagenReferenciaEntity::getUrl)
                .toList();
    }

    @Override
    public void reemplazar(TipoImagenReferencia tipo, String propietarioId, List<String> urls) {
        dataRepository.deleteByTipoAndIdPropietario(tipo, propietarioId);

        List<ImagenReferenciaEntity> nuevas = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            ImagenReferenciaEntity entidad = new ImagenReferenciaEntity();
            entidad.setTipo(tipo);
            entidad.setIdPropietario(propietarioId);
            entidad.setUrl(urls.get(i));
            entidad.setOrden(i);
            nuevas.add(entidad);
        }
        dataRepository.saveAll(nuevas);
    }
}