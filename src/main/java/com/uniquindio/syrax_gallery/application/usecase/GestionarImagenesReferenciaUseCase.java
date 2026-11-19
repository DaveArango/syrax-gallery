package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.ImagenReferenciaRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.TipoImagenReferencia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GestionarImagenesReferenciaUseCase {

    private final ImagenReferenciaRepository imagenRepository;
    private final CaraxesRepository caraxesRepository;
    private final SunfyreRepository sunfyreRepository;

    @Transactional(readOnly = true)
    public List<String> listar(TipoImagenReferencia tipo, String propietarioId) {
        return imagenRepository.listar(tipo, propietarioId);
    }

    @Transactional
    public List<String> reemplazar(TipoImagenReferencia tipo, String propietarioId,
                                   String azantysId, List<String> urls) {
        String idDuenio = switch (tipo) {
            case CARAXES -> caraxesRepository.obtener(propietarioId)
                    .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"))
                    .getIdAzantys();
            case SUNFYRE -> sunfyreRepository.obtener(propietarioId)
                    .orElseThrow(() -> new NoSuchElementException("Sunfyre no encontrada"))
                    .getIdAzantys();
        };
        if (!idDuenio.equals(azantysId))
            throw new ReglaDominioException("Solo el Azantys dueño puede cambiar las imágenes de referencia.");

        imagenRepository.reemplazar(tipo, propietarioId, urls);
        return urls;
    }
}