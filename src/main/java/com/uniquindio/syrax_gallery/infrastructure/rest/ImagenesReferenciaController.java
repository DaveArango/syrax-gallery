package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.GuardarImagenesReferenciaRequest;
import com.uniquindio.syrax_gallery.application.dto.response.ImagenesReferenciaResponse;
import com.uniquindio.syrax_gallery.application.usecase.GestionarImagenesReferenciaUseCase;
import com.uniquindio.syrax_gallery.domain.valueobject.TipoImagenReferencia;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImagenesReferenciaController {

    private final GestionarImagenesReferenciaUseCase useCase;

    @GetMapping("/caraxes/{id}/imagenes")
    public ImagenesReferenciaResponse imagenesDeCaraxes(@PathVariable String id) {
        return new ImagenesReferenciaResponse(useCase.listar(TipoImagenReferencia.CARAXES, id));
    }

    @PutMapping("/caraxes/{id}/imagenes")
    public ImagenesReferenciaResponse guardarImagenesDeCaraxes(@PathVariable String id,
                                                               @Valid @RequestBody GuardarImagenesReferenciaRequest request,
                                                               JwtAuthenticationToken token) {
        return new ImagenesReferenciaResponse(
                useCase.reemplazar(TipoImagenReferencia.CARAXES, id, UsuarioActual.id(token), request.urls()));
    }

    @GetMapping("/sunfyre/{id}/imagenes")
    public ImagenesReferenciaResponse imagenesDeSunfyre(@PathVariable String id) {
        return new ImagenesReferenciaResponse(useCase.listar(TipoImagenReferencia.SUNFYRE, id));
    }

    @PutMapping("/sunfyre/{id}/imagenes")
    public ImagenesReferenciaResponse guardarImagenesDeSunfyre(@PathVariable String id,
                                                               @Valid @RequestBody GuardarImagenesReferenciaRequest request,
                                                               JwtAuthenticationToken token) {
        return new ImagenesReferenciaResponse(
                useCase.reemplazar(TipoImagenReferencia.SUNFYRE, id, UsuarioActual.id(token), request.urls()));
    }
}