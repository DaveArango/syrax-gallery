package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.CrearSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.request.PublicarSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.response.SunfyreDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.CrearSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.PublicarSunfyreUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.SunfyreMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/sunfyre")
public class SunfyreController {

    private final PublicarSunfyreUseCase publicarSunfyreUseCase;
    private final CrearSunfyreUseCase crearSunfyreUseCase;
    private final SunfyreMapper mapper;

    public SunfyreController(CrearSunfyreUseCase crearSunfyreUseCase,
                             PublicarSunfyreUseCase publicarSunfyreUseCase,
                             SunfyreMapper mapper) {
        this.crearSunfyreUseCase = crearSunfyreUseCase;
        this.publicarSunfyreUseCase = publicarSunfyreUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<SunfyreDetalleResponse> crear(@Valid @RequestBody CrearSunfyreRequest request) {
        Sunfyre sunfyre = crearSunfyreUseCase.ejecutar(
                UUID.randomUUID().toString(),
                request.azantysId(),
                request.zentysId(),
                new Sari(request.inicio(), request.fin()),
                request.kastor(),
                new Alion(request.ciudad(), request.pais(), request.latitud(), request.longitud()),
                new Vala(request.monto(), request.divisa()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sunfyre.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDetalleResponse(sunfyre));
    }

    @PutMapping("/{id}/publicar")
    public ResponseEntity<SunfyreDetalleResponse> publicar(@PathVariable String id,
                                                           @Valid @RequestBody PublicarSunfyreRequest request) {
        Sunfyre sunfyre = publicarSunfyreUseCase.ejecutar(request.azantysId(), id, request.destino());
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }
}
