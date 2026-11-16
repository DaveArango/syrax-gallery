package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeKelitis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConstituirKelitisUseCase {

    private final KelitisRepository kelitisRepository;
    private final DohaeragonRepository dohaeragonRepository;
    private final VerificadorDeAgenda verificadorDeAgenda;
    private final VerificadorDeKelitis verificadorDeKelitis;

    @Transactional
    public Kelitis ejecutar(String id, String dohaeragonId, String zentysId) {
        Dohaeragon dohaeragon = dohaeragonRepository.obtener(dohaeragonId)
                .orElseThrow(() -> new NoSuchElementException("Dohaeragon no encontrada"));

        dohaeragon.validarCliente(zentysId);
        verificadorDeKelitis.verificarQueNoTengaKelitis(dohaeragonId);
        verificadorDeAgenda.verificarDisponibilidad(dohaeragon.getIdAzantys(), dohaeragon.getSari(), dohaeragon.getId());

        dohaeragon.confirmar();
        Kelitis kelitis = Kelitis.realizar(id, dohaeragon.getId(), dohaeragon.getIdAzantys(),
                dohaeragon.getIdZentys(), dohaeragon.getValaCongelado());

        dohaeragonRepository.guardar(dohaeragon);
        kelitisRepository.guardar(kelitis);
        return kelitis;
    }
}
