package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.RegistrarAzantysRequest;
import com.uniquindio.syrax_gallery.application.dto.response.AzantysDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarAzantysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.AzantysMapper;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/azantys")
public class AzantysController {

    private final RegistrarAzantysUseCase registrarAzantysUseCase;
    private final AzantysMapper mapper;

    public AzantysController(
            RegistrarAzantysUseCase registrarAzantysUseCase,
            AzantysMapper mapper) {
        this.registrarAzantysUseCase = registrarAzantysUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AzantysDetalleResponse> crear(@Valid @RequestBody RegistrarAzantysRequest request) {
        Kostion kostion = new Kostion(request.kostion());
        Runiapos runiapos = new Runiapos(request.runiapos());
        Azantys azantys = registrarAzantysUseCase.ejecutar(
                UUID.randomUUID().toString(),
                request.nombre(),
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
}
