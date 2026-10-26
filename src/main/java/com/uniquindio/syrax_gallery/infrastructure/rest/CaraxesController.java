package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.RegistrarCaraxesRequest;
import com.uniquindio.syrax_gallery.application.dto.response.CaraxesDetalleResponse;
import com.uniquindio.syrax_gallery.application.dto.response.PageResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarCatalogoCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.PublicarCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarCaraxesUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.CaraxesMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/caraxes")
public class CaraxesController {
    private final ObtenerCaraxesUseCase obtenerCaraxesUseCase;
    private final RegistrarCaraxesUseCase registrarCaraxesUseCase;
    private final PublicarCaraxesUseCase publicarCaraxesUseCase;
    private final ConsultarCatalogoCaraxesUseCase consultarCatalogoCaraxesUseCase;
    private final CaraxesMapper mapper;

    public CaraxesController(ObtenerCaraxesUseCase obtenerCaraxesUseCase,
                             RegistrarCaraxesUseCase registrarCaraxesUseCase,
                             PublicarCaraxesUseCase publicarCaraxesUseCase,
                             ConsultarCatalogoCaraxesUseCase consultarCatalogoCaraxesUseCase,
                             CaraxesMapper mapper) {
        this.obtenerCaraxesUseCase = obtenerCaraxesUseCase;
        this.registrarCaraxesUseCase = registrarCaraxesUseCase;
        this.publicarCaraxesUseCase = publicarCaraxesUseCase;
        this.consultarCatalogoCaraxesUseCase = consultarCatalogoCaraxesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CaraxesDetalleResponse> crear (@Valid @RequestBody RegistrarCaraxesRequest request){
        Vala vala = new Vala(
                request.monto(),
                request.divisa()
        );
        Sete sete = new Sete(
                request.sete()
        );
        Jorva jorva = new Jorva(
                request.jorva()
        );
        Urnebion urnebion = new Urnebion(
                request.urnebion()
        );
        CaraxesKastor kastor = CaraxesKastor.desde(request.kastor());

        Caraxes caraxes = registrarCaraxesUseCase.ejecutar(
                UUID.randomUUID().toString(),
                request.azantysId(),
                vala,
                sete,
                jorva,
                urnebion,
                kastor);

        CaraxesDetalleResponse response = mapper.toDetalleResponse(caraxes);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(caraxes.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(response);
    }

    @PutMapping("/{id}/publicar/{kanez}")
    public ResponseEntity<CaraxesDetalleResponse> publicar(@PathVariable String id,
                                                           @PathVariable Kanez kanez) {
        Caraxes caraxes = publicarCaraxesUseCase.ejecutar(id, kanez);
        CaraxesDetalleResponse response = mapper.toDetalleResponse(caraxes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaraxesDetalleResponse> obtener (@PathVariable String id){
        Caraxes caraxes = obtenerCaraxesUseCase.ejecutar(id);
        CaraxesDetalleResponse response = mapper.toDetalleResponse(caraxes);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public PageResponse<CaraxesDetalleResponse> buscar(
            @RequestParam(name = "texto", required = false) String texto,
            @RequestParam(name = "kastor", required = false) CaraxesKastor kastor,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return PageResponse.desde(
                consultarCatalogoCaraxesUseCase.buscar(texto, kastor, page, size),
                mapper::toDetalleResponse);
    }
}
