package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysServicios;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarZentysUseCase {

    private static final int TAMANO_MAXIMO = 50;
    private static final int RANKING_MAXIMO = 20;

    private final ZentysRepository repository;

    @Transactional(readOnly = true)
    public Pagina<Zentys> buscar(String texto,
                                 int pagina,
                                 int tamano) {
        if (pagina < 0) {
            throw new ReglaDominioException("La página no puede ser negativa");
        }
        if (tamano < 1 || tamano > TAMANO_MAXIMO) {
            throw new ReglaDominioException("El tamaño de la página debe estar entre 1 y " + TAMANO_MAXIMO);
        }
        return repository.buscarActivos(texto, pagina, tamano);
    }

    @Transactional(readOnly = true)
    public List<ZentysServicios> masActivos(int limite) {
        if (limite < 1 || limite > RANKING_MAXIMO) {
            throw new ReglaDominioException("El límite debe estar entre 1 y " + RANKING_MAXIMO);
        }
        return repository.masActivos(limite);
    }
}
