package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.CaraxesDetalleResponse;
import com.uniquindio.syrax_gallery.application.dto.response.KelitisDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarCaraxesDeAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarKelitisDeZentysUseCase;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.CaraxesMapper;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.KelitisMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Listas "mías" que usan las páginas propias de cada rol en el front.
 * El dueño siempre sale del token, nunca de un parámetro.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PanelController {

    private final ConsultarKelitisDeZentysUseCase consultarKelitisDeZentysUseCase;
    private final ConsultarCaraxesDeAzantysUseCase consultarCaraxesDeAzantysUseCase;
    private final KelitisMapper kelitisMapper;
    private final CaraxesMapper caraxesMapper;

    // Zentys: sus pagos de servicios
    @GetMapping("/kelitis/mis-pagos")
    public List<KelitisDetalleResponse> misPagos(JwtAuthenticationToken token) {
        return consultarKelitisDeZentysUseCase.ejecutar(UsuarioActual.id(token)).stream()
                .map(kelitisMapper::toDetalleResponse)
                .toList();
    }

    // Azantys: todas sus obras, en cualquier estado
    @GetMapping("/caraxes/mis-obras")
    public List<CaraxesDetalleResponse> misObras(JwtAuthenticationToken token) {
        return consultarCaraxesDeAzantysUseCase.ejecutar(UsuarioActual.id(token)).stream()
                .map(caraxesMapper::toDetalleResponse)
                .toList();
    }
}
