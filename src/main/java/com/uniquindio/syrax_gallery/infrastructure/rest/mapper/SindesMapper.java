package com.uniquindio.syrax_gallery.infrastructure.rest.mapper;

import com.uniquindio.syrax_gallery.application.dto.response.SindesDetalleResponse;
import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import org.springframework.stereotype.Component;

@Component
public class SindesMapper {

    public SindesDetalleResponse toDetalleResponse(Sindes sindes) {
        return new SindesDetalleResponse(
                sindes.getId(),
                sindes.getIdCaraxes(),
                sindes.getIdAzantys(),
                sindes.getIdZentys(),
                sindes.getValaCongelado().monto(),
                sindes.getValaCongelado().divisa(),
                sindes.getFecha(),
                sindes.getSindesJeda().name()
        );
    }
}