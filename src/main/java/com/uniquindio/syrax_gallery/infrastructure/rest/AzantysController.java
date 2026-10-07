package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.RegistrarAzantysRequest;
import com.uniquindio.syrax_gallery.application.dto.request.VerificarIdentidadRequest;
import com.uniquindio.syrax_gallery.application.dto.response.AzantysDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.VerificarIdentidadAzantysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.AzantysMapper;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/azantys")
public class AzantysController {

    private final RegistrarAzantysUseCase registrarAzantysUseCase;
    private final VerificarIdentidadAzantysUseCase verificarIdentidadAzantysUseCase;
    private final AzantysMapper mapper;

    public AzantysController(
            RegistrarAzantysUseCase registrarAzantysUseCase,
            VerificarIdentidadAzantysUseCase verificarIdentidadAzantysUseCase,
            AzantysMapper mapper) {
        this.registrarAzantysUseCase = registrarAzantysUseCase;
        this.verificarIdentidadAzantysUseCase = verificarIdentidadAzantysUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AzantysDetalleResponse> crear(@Valid @RequestBody RegistrarAzantysRequest request) {
        Kostion kostion = new Kostion(request.kostion());
        Runiapos runiapos = new Runiapos(request.runiapos());
        Azantys azantys = registrarAzantysUseCase.ejecutar(request.id(), request.nombre(),
                kostion, runiapos, request.lentorId());

        AzantysDetalleResponse response =
                mapper.toDetalleResponse(azantys);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(azantys.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}/identidad")
    public ResponseEntity<AzantysDetalleResponse> verificarIdentidad(
            @PathVariable String id,
            @Valid @RequestBody VerificarIdentidadRequest request) {
        Azantys azantys = verificarIdentidadAzantysUseCase.ejecutar(id, new Iksia(request.iksia()));
        return ResponseEntity.ok(mapper.toDetalleResponse(azantys));
    }
}
