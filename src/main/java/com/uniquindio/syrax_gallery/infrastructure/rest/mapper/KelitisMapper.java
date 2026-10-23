package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.KelitisDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import org.springframework.stereotype.Component;

@Component
public class KelitisMapper {

    public KelitisDetalleResponse toDetalleResponse(Kelitis kelitis) {
        return new KelitisDetalleResponse(
                kelitis.getId(),
                kelitis.getIdSunfyre(),
                kelitis.getIdAzantys(),
                kelitis.getIdZentys(),
                kelitis.getValaCongelado().monto(),
                kelitis.getValaCongelado().divisa(),
                kelitis.getGelior().name()
        );
    }
}
