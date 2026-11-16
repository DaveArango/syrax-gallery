package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.SunfyreDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SunfyreMapper {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    public SunfyreDetalleResponse toDetalleResponse(Sunfyre sunfyre) {
        return new SunfyreDetalleResponse(
                sunfyre.getId(),
                sunfyre.getIdAzantys(),
                sunfyre.getSete().nombre(),
                sunfyre.getJorva().descripcion(),
                sunfyre.getUrnebion().url(),
                sunfyre.getKastor().name(),
                sunfyre.getAlion().ciudad(),
                sunfyre.getAlion().pais(),
                sunfyre.getVala().monto(),
                sunfyre.getVala().divisa(),
                sunfyre.getDuracionMinutos(),
                sunfyre.getJedar().dias().stream().sorted().map(Enum::name).toList(),
                sunfyre.getJedar().apertura().format(HORA),
                sunfyre.getJedar().cierre().format(HORA),
                sunfyre.isActivo()
        );
    }
}
