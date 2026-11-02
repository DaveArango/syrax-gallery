package com.uniquindio.syrax_gallery.domain.service;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;

public class VerificadorDeKelitis {

    private final KelitisRepository kelitisRepository;

    public VerificadorDeKelitis(KelitisRepository kelitisRepository) {
        this.kelitisRepository = kelitisRepository;
    }

    public void verificarQueNoTengaKelitis(String idSunfyre) {
        if (kelitisRepository.existePorSunfyre(idSunfyre)) {
            throw new ReglaDominioException("La Sunfyre ya tiene un Kelitis constituido");
        }
    }
}
