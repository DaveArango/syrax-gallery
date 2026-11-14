package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeSindes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RealizarSindesUseCase {

    private final SindesRepository sindesRepository;
    private final CaraxesRepository caraxesRepository;
    private final ZentysRepository zentysRepository;
    private final VerificadorDeSindes verificadorDeSindes;

    @Transactional
    public Sindes ejecutar(String id, String zentysId, String caraxesId) {
        Zentys zentys = zentysRepository.obtener(zentysId)
                .orElseThrow(() -> new NoSuchElementException("Zentys no encontrado"));
        Caraxes caraxes = caraxesRepository.obtener(caraxesId)
                .orElseThrow(() -> new NoSuchElementException("Caraxes no encontrado"));

        zentys.validarPuedeComprar();
        caraxes.validarEnVenta();
        verificadorDeSindes.verificarQueNoTengaSindesPendiente(caraxesId);

        // El Vala se toma de la obra en este momento y queda congelado en el Sindes
        Sindes sindes = Sindes.realizar(id, caraxes.getId(), caraxes.getIdAzantys(), zentysId, caraxes.getVala());
        sindesRepository.guardar(sindes);
        return sindes;
    }
}
