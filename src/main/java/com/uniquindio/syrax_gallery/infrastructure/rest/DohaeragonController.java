package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.CancelarDohaeragonRequest;
import com.uniquindio.syrax_gallery.application.dto.request.CrearDohaeragonRequest;
import com.uniquindio.syrax_gallery.application.dto.request.PublicarDohaeragonRequest;
import com.uniquindio.syrax_gallery.application.dto.request.ReprogramarDohaeragonRequest;
import com.uniquindio.syrax_gallery.application.dto.response.PageResponse;
import com.uniquindio.syrax_gallery.application.dto.response.DohaeragonDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.CancelarDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarCatalogoDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.CrearDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.FinalizarDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.IniciarEjecucionDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.PublicarDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ReprogramarDohaeragonUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarDohaeragonDeAzantysUseCase;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.DohaeragonMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/dohaeragon")
public class DohaeragonController {

    private final PublicarDohaeragonUseCase publicarDohaeragonUseCase;
    private final CrearDohaeragonUseCase crearDohaeragonUseCase;
    private final ObtenerDohaeragonUseCase obtenerDohaeragonUseCase;
    private final ConsultarCatalogoDohaeragonUseCase consultarCatalogoDohaeragonUseCase;
    private final IniciarEjecucionDohaeragonUseCase iniciarEjecucionDohaeragonUseCase;
    private final FinalizarDohaeragonUseCase finalizarDohaeragonUseCase;
    private final CancelarDohaeragonUseCase cancelarDohaeragonUseCase;
    private final ReprogramarDohaeragonUseCase reprogramarDohaeragonUseCase;
    private final ConsultarDohaeragonDeAzantysUseCase consultarDohaeragonDeAzantysUseCase;
    private final DohaeragonMapper mapper;

    public DohaeragonController(CrearDohaeragonUseCase crearDohaeragonUseCase,
                             ObtenerDohaeragonUseCase obtenerDohaeragonUseCase,
                             PublicarDohaeragonUseCase publicarDohaeragonUseCase,
                             ConsultarCatalogoDohaeragonUseCase consultarCatalogoDohaeragonUseCase,
                             IniciarEjecucionDohaeragonUseCase iniciarEjecucionDohaeragonUseCase,
                             FinalizarDohaeragonUseCase finalizarDohaeragonUseCase,
                             CancelarDohaeragonUseCase cancelarDohaeragonUseCase,
                             ReprogramarDohaeragonUseCase reprogramarDohaeragonUseCase,
                             ConsultarDohaeragonDeAzantysUseCase consultarDohaeragonDeAzantysUseCase,
                             DohaeragonMapper mapper) {
        this.obtenerDohaeragonUseCase = obtenerDohaeragonUseCase;
        this.crearDohaeragonUseCase = crearDohaeragonUseCase;
        this.publicarDohaeragonUseCase = publicarDohaeragonUseCase;
        this.consultarCatalogoDohaeragonUseCase = consultarCatalogoDohaeragonUseCase;
        this.finalizarDohaeragonUseCase = finalizarDohaeragonUseCase;
        this.iniciarEjecucionDohaeragonUseCase = iniciarEjecucionDohaeragonUseCase;
        this.cancelarDohaeragonUseCase = cancelarDohaeragonUseCase;
        this.reprogramarDohaeragonUseCase = reprogramarDohaeragonUseCase;
        this.consultarDohaeragonDeAzantysUseCase = consultarDohaeragonDeAzantysUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<DohaeragonDetalleResponse> crear(@Valid @RequestBody CrearDohaeragonRequest request,
                                                        JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = crearDohaeragonUseCase.ejecutar(
                UUID.randomUUID().toString(),
                UsuarioActual.id(token),
                request.zentysId(),
                new Sari(request.inicio(), request.fin()),
                request.kastor(),
                new Alion(request.ciudad(), request.pais(), request.latitud(), request.longitud()),
                new Vala(request.monto(), request.divisa()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dohaeragon.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDetalleResponse(dohaeragon));
    }

    @GetMapping
    public PageResponse<DohaeragonDetalleResponse> buscar(
            @RequestParam(name = "ciudad", required = false) String ciudad,
            @RequestParam(name = "kastor", required = false) SunfyreKastor kastor,
            @RequestParam(name = "precioMin", required = false) Double precioMin,
            @RequestParam(name = "precioMax", required = false) Double precioMax,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return PageResponse.desde(
                consultarCatalogoDohaeragonUseCase.buscar(ciudad, kastor, precioMin, precioMax, page, size),
                mapper::toDetalleResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DohaeragonDetalleResponse> obtener(@PathVariable String id,
                                                          JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = obtenerDohaeragonUseCase.ejecutar(id, UsuarioActual.id(token));
        return ResponseEntity.ok(mapper.toDetalleResponse(dohaeragon));
    }

    @PutMapping("/{id}/publicar")
    public ResponseEntity<DohaeragonDetalleResponse> publicar(@PathVariable String id,
                                                           @Valid @RequestBody PublicarDohaeragonRequest request,
                                                           JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = publicarDohaeragonUseCase.ejecutar(UsuarioActual.id(token), id, request.destino());
        return ResponseEntity.ok(mapper.toDetalleResponse(dohaeragon));
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<DohaeragonDetalleResponse> iniciarEjecucion(@PathVariable String id,
                                                                   JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = iniciarEjecucionDohaeragonUseCase.ejecutar(UsuarioActual.id(token), id);
        return ResponseEntity.ok(mapper.toDetalleResponse(dohaeragon));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<DohaeragonDetalleResponse> finalizar(@PathVariable String id,
                                                            JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = finalizarDohaeragonUseCase.ejecutar(UsuarioActual.id(token), id);
        return ResponseEntity.ok(mapper.toDetalleResponse(dohaeragon));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<DohaeragonDetalleResponse> cancelar(@PathVariable String id,
                                                           @Valid @RequestBody CancelarDohaeragonRequest request,
                                                           JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = cancelarDohaeragonUseCase.ejecutar(id, UsuarioActual.id(token), request.motivo());
        return ResponseEntity.ok(mapper.toDetalleResponse(dohaeragon));
    }

    @PutMapping("/{id}/reprogramar")
    public ResponseEntity<DohaeragonDetalleResponse> reprogramar(@PathVariable String id,
                                                              @Valid @RequestBody ReprogramarDohaeragonRequest request,
                                                              JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = reprogramarDohaeragonUseCase.ejecutar(
                UsuarioActual.id(token), id, new Sari(request.inicio(), request.fin()));
        return ResponseEntity.ok(mapper.toDetalleResponse(dohaeragon));
    }

    @GetMapping("/mis-servicios")
    public PageResponse<DohaeragonDetalleResponse> misServicios(
            @RequestParam(name = "estado", required = false) DohaeroxJeda estado,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            JwtAuthenticationToken token) {
        return PageResponse.desde(
                consultarDohaeragonDeAzantysUseCase.ejecutar(UsuarioActual.id(token), estado, page, size),
                mapper::toDetalleResponse);
    }
}
