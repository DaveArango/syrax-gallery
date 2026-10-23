package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.AzantysServicios;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConsultarAzantysUseCase {

    private static final int TAMANO_MAXIMO = 50;
    private static final int RANKING_MAXIMO = 20;

    private final AzantysRepository repository;

    @Transactional(readOnly = true)
    public Pagina<Azantys> buscar(String texto, int pagina, int tamano) {
        if (pagina < 0) {
            throw new ReglaDominioException("La página no puede ser negativa");
        }
        if (tamano < 1 || tamano > TAMANO_MAXIMO) {
            throw new ReglaDominioException("El tamaño de la página debe estar entre 1 y " + TAMANO_MAXIMO);
        }
        return repository.buscarHabilitados(texto, pagina, tamano);
    }

    @Transactional(readOnly = true)
    public Azantys obtener(String id) {
        return repository.obtener(id)
                .filter(Azantys::isActivo)
                .filter(Azantys::estaHabilitadoParaPublicar)
                .orElseThrow(() -> new NoSuchElementException("Azantys no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<AzantysServicios> masSolicitados(int limite) {
        if (limite < 1 || limite > RANKING_MAXIMO) {
            throw new ReglaDominioException("El límite debe estar entre 1 y " + RANKING_MAXIMO);
        }
        return repository.masSolicitados(limite);
    }
}
