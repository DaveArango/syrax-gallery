package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Gida;
import com.uniquindio.syrax_gallery.domain.repository.GidaRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeSindes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DejarGidaUseCase {

    private final GidaRepository gidaRepository;
    private final VerificadorDeSindes verificadorDeSindes;

    @Transactional
    public Gida ejecutar(String id, String zentysId, String caraxesId, String texto) {
        verificadorDeSindes.verificarQuePuedeDejarGida(zentysId, caraxesId);

        Gida gida = Gida.crear(id, zentysId, caraxesId, texto);
        gidaRepository.guardar(gida);
        return gida;
    }
}