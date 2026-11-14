package com.uniquindio.syrax_gallery.domain.service;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;

/**
 * Servicio de dominio: reglas que necesitan mirar otros Sindes guardados,
 * así que no caben dentro de un solo Sindes ni de un solo Gida.
 */
public class VerificadorDeSindes {

    private final SindesRepository sindesRepository;

    public VerificadorDeSindes(SindesRepository sindesRepository) {
        this.sindesRepository = sindesRepository;
    }

    public void verificarQueNoTengaSindesPendiente(String idCaraxes) {
        if (sindesRepository.existePendientePorCaraxes(idCaraxes))
            throw new ReglaDominioException("Este Caraxes ya tiene un Sindes pendiente");
    }

    public void verificarQuePuedeDejarGida(String idZentys, String idCaraxes) {
        if (!sindesRepository.existeCompletada(idZentys, idCaraxes))
            throw new ReglaDominioException("Solo puede dejar un Gida quien ya compró este Caraxes");
    }
}
