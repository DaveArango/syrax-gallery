package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.ConstituirKelitisRequest;
import com.uniquindio.syrax_gallery.application.dto.response.KelitisDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.AprobarEjecucionKelitisUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConstituirKelitisUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerKelitisUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.KelitisMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/kelitis")
public class KelitisController {

    private final ConstituirKelitisUseCase constituirKelitisUseCase;
    private final ObtenerKelitisUseCase obtenerKelitisUseCase;
    private final AprobarEjecucionKelitisUseCase aprobarEjecucionUseCase;
    private final KelitisMapper mapper;

    public KelitisController(ConstituirKelitisUseCase constituirKelitisUseCase,
                             ObtenerKelitisUseCase obtenerKelitisUseCase,
                             AprobarEjecucionKelitisUseCase aprobarEjecucionUseCase,
                             KelitisMapper mapper) {
        this.constituirKelitisUseCase = constituirKelitisUseCase;
        this.obtenerKelitisUseCase = obtenerKelitisUseCase;
        this.aprobarEjecucionUseCase = aprobarEjecucionUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<KelitisDetalleResponse> constituir(@Valid @RequestBody ConstituirKelitisRequest request,
                                                             JwtAuthenticationToken token) {
        Kelitis kelitis = constituirKelitisUseCase.ejecutar(
                UUID.randomUUID().toString(), request.sunfyreId(), UsuarioActual.id(token));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(kelitis.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDetalleResponse(kelitis)); // 201
    }

    @GetMapping("/{id}")
    public ResponseEntity<KelitisDetalleResponse> obtener(@PathVariable String id,
                                                          JwtAuthenticationToken token) {
        Kelitis kelitis = obtenerKelitisUseCase.ejecutar(id, UsuarioActual.id(token));
        return ResponseEntity.ok(mapper.toDetalleResponse(kelitis));
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<KelitisDetalleResponse> aprobar(@PathVariable String id,
                                                          JwtAuthenticationToken token) {
        Kelitis kelitis = aprobarEjecucionUseCase.ejecutar(id, UsuarioActual.id(token));
        return ResponseEntity.ok(mapper.toDetalleResponse(kelitis));
    }
}