package com.uniquindio.syrax_gallery.domain.service;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;

public class VerificadorDeKelitis {

    private final KelitisRepository kelitisRepository;

    public VerificadorDeKelitis(KelitisRepository kelitisRepository) {
        this.kelitisRepository = kelitisRepository;
    }

    public void verificarQueNoTengaKelitis(String idDohaeragon) {
        if (kelitisRepository.existePorDohaeragon(idDohaeragon)) {
            throw new ReglaDominioException("La Dohaeragon ya tiene un Kelitis constituido");
        }
    }
}
