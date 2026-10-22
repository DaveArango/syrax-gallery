package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.RegistrarZentysRequest;
import com.uniquindio.syrax_gallery.application.dto.response.ZentysDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerZentysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarZentysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.ZentysMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/zentys")
public class ZentysController {
    private final RegistrarZentysUseCase registrarZentysUseCase;
    private final ObtenerZentysUseCase obtenerZentysUseCase;
    private final ZentysMapper mapper;

    public ZentysController(RegistrarZentysUseCase registrarZentysUseCase,
                            ObtenerZentysUseCase obtenerZentysUseCase,
                            ZentysMapper mapper) {
        this.registrarZentysUseCase = registrarZentysUseCase;
        this.obtenerZentysUseCase = obtenerZentysUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ZentysDetalleResponse> crear (@Valid @RequestBody RegistrarZentysRequest request){
        Runiapos runiapos = new Runiapos(request.runiapos());
        Indior indior = new Indior(request.indior());

        Zentys zentys = registrarZentysUseCase.ejecutar(
                UUID.randomUUID().toString(),
                request.nombre(),
                runiapos,
                indior);

        ZentysDetalleResponse response = mapper.toDetalleResponse(zentys);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(zentys.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZentysDetalleResponse> obtener (@PathVariable String id){
        Zentys zentys = obtenerZentysUseCase.ejecutar(id);
        ZentysDetalleResponse response = mapper.toDetalleResponse(zentys);

        return ResponseEntity.ok(response);
    }
}
