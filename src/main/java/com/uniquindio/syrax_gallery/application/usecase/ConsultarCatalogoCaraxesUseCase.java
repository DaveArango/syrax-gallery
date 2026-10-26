package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.CaraxesKastor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarCatalogoCaraxesUseCase {

    private static final int TAMANO_MAXIMO = 50;

    private final CaraxesRepository repository;

    @Transactional(readOnly = true)
    public Pagina<Caraxes> buscar(String texto,
                                  CaraxesKastor kastor,
                                  int pagina,
                                  int tamano) {
        if (pagina < 0) {
            throw new ReglaDominioException("La página no puede ser negativa");
        }
        if (tamano < 1 || tamano > TAMANO_MAXIMO) {
            throw new ReglaDominioException("El tamaño de la página debe estar entre 1 y " + TAMANO_MAXIMO);
        }
        return repository.buscarPublicadas(texto, kastor, pagina, tamano);
    }
}
