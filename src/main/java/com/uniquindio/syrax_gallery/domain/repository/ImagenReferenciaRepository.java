package com.uniquindio.syrax_gallery.domain.repository;

import com.uniquindio.syrax_gallery.domain.valueobject.TipoImagenReferencia;

import java.util.List;

public interface ImagenReferenciaRepository {

    List<String> listar(TipoImagenReferencia tipo, String propietarioId);

    void reemplazar(TipoImagenReferencia tipo, String propietarioId, List<String> urls);
}