package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarCatalogoSunfyreUseCase {

    private static final int TAMANO_MAXIMO = 50;

    private final SunfyreRepository repository;

    @Transactional(readOnly = true)
    public Pagina<Sunfyre> buscar(String ciudad, SunfyreKastor kastor,
                                  Double precioMin, Double precioMax, int pagina, int tamano) {
        if (pagina < 0) {
            throw new ReglaDominioException("La página no puede ser negativa");
        }
        if (tamano < 1 || tamano > TAMANO_MAXIMO) {
            throw new ReglaDominioException("El tamaño de la página debe estar entre 1 y " + TAMANO_MAXIMO);
        }
        if ((precioMin != null && precioMin < 0) || (precioMax != null && precioMax < 0)) {
            throw new ReglaDominioException("El precio no puede ser negativo");
        }
        if (precioMin != null && precioMax != null && precioMin > precioMax) {
            throw new ReglaDominioException("El precio mínimo no puede ser mayor que el máximo");
        }
        return repository.buscarPublicadas(ciudad, kastor, precioMin, precioMax, pagina, tamano);
    }
}
