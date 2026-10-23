package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.ConstituirKelitisRequest;
import com.uniquindio.syrax_gallery.application.dto.response.KelitisDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConstituirKelitisUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerKelitisUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.KelitisMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/kelitis")
public class KelitisController {

    private final ConstituirKelitisUseCase constituirKelitisUseCase;
    private final ObtenerKelitisUseCase obtenerKelitisUseCase;
    private final KelitisMapper mapper;

    public KelitisController(ConstituirKelitisUseCase constituirKelitisUseCase,
                             ObtenerKelitisUseCase obtenerKelitisUseCase,
                             KelitisMapper mapper) {
        this.constituirKelitisUseCase = constituirKelitisUseCase;
        this.obtenerKelitisUseCase = obtenerKelitisUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<KelitisDetalleResponse> constituir(@Valid @RequestBody ConstituirKelitisRequest request) {
        Kelitis kelitis = constituirKelitisUseCase.ejecutar(UUID.randomUUID().toString(), request.sunfyreId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(kelitis.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDetalleResponse(kelitis)); // 201
    }

    @GetMapping("/{id}")
    public ResponseEntity<KelitisDetalleResponse> obtener(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDetalleResponse(obtenerKelitisUseCase.ejecutar(id)));
    }

}
