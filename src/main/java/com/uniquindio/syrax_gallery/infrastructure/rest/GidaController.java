package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.DejarGidaRequest;
import com.uniquindio.syrax_gallery.application.dto.response.GidaResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarGidaDeCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.DejarGidaUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Gida;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.GidaMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gida")
@RequiredArgsConstructor
public class GidaController {

    private final DejarGidaUseCase dejarGidaUseCase;
    private final ConsultarGidaDeCaraxesUseCase consultarGidaDeCaraxesUseCase;
    private final GidaMapper mapper;

    // Zentys: comentar una obra que ya compró
    @PostMapping
    public ResponseEntity<GidaResponse> dejar(@Valid @RequestBody DejarGidaRequest request,
                                              JwtAuthenticationToken token) {
        Gida gida = dejarGidaUseCase.ejecutar(
                UUID.randomUUID().toString(), UsuarioActual.id(token), request.caraxesId(), request.texto());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(gida)); // 201
    }

    // Público: ver los Gida de una obra, del más reciente al más antiguo
    @GetMapping
    public List<GidaResponse> deCaraxes(@RequestParam("caraxesId") String caraxesId) {
        return consultarGidaDeCaraxesUseCase.ejecutar(caraxesId).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
