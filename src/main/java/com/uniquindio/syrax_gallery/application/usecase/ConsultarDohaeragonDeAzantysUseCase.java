package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarDohaeragonDeAzantysUseCase {

    private static final int TAMANO_MAXIMO = 50;

    private final DohaeragonRepository repository;

    @Transactional(readOnly = true)
    public Pagina<Dohaeragon> ejecutar(String azantysId, DohaeroxJeda estado, int pagina, int tamano) {
        if (pagina < 0) {
            throw new IllegalArgumentException("La página no puede ser negativa");
        }
        if (tamano < 1 || tamano > TAMANO_MAXIMO) {
            throw new IllegalArgumentException("El tamaño de la página debe estar entre 1 y " + TAMANO_MAXIMO);
        }
        return repository.buscarDeAzantys(azantysId, estado, pagina, tamano);
    }
}
