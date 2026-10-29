package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.CancelarSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.request.CrearSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.request.PublicarSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.request.ReprogramarSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.response.PageResponse;
import com.uniquindio.syrax_gallery.application.dto.response.SunfyreDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.CancelarSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarCatalogoSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.CrearSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.FinalizarSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.IniciarEjecucionSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.PublicarSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ReprogramarSunfyreUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Sari;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.SunfyreMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/sunfyre")
public class SunfyreController {

    private final PublicarSunfyreUseCase publicarSunfyreUseCase;
    private final CrearSunfyreUseCase crearSunfyreUseCase;
    private final ObtenerSunfyreUseCase obtenerSunfyreUseCase;
    private final ConsultarCatalogoSunfyreUseCase consultarCatalogoSunfyreUseCase;
    private final IniciarEjecucionSunfyreUseCase iniciarEjecucionSunfyreUseCase;
    private final FinalizarSunfyreUseCase finalizarSunfyreUseCase;
    private final CancelarSunfyreUseCase cancelarSunfyreUseCase;
    private final ReprogramarSunfyreUseCase reprogramarSunfyreUseCase;
    private final SunfyreMapper mapper;

    public SunfyreController(CrearSunfyreUseCase crearSunfyreUseCase,
                             ObtenerSunfyreUseCase obtenerSunfyreUseCase,
                             PublicarSunfyreUseCase publicarSunfyreUseCase,
                             ConsultarCatalogoSunfyreUseCase consultarCatalogoSunfyreUseCase,
                             IniciarEjecucionSunfyreUseCase iniciarEjecucionSunfyreUseCase,
                             FinalizarSunfyreUseCase finalizarSunfyreUseCase,
                             CancelarSunfyreUseCase cancelarSunfyreUseCase,
                             ReprogramarSunfyreUseCase reprogramarSunfyreUseCase,
                             SunfyreMapper mapper) {
        this.obtenerSunfyreUseCase = obtenerSunfyreUseCase;
        this.crearSunfyreUseCase = crearSunfyreUseCase;
        this.publicarSunfyreUseCase = publicarSunfyreUseCase;
        this.consultarCatalogoSunfyreUseCase = consultarCatalogoSunfyreUseCase;
        this.finalizarSunfyreUseCase = finalizarSunfyreUseCase;
        this.iniciarEjecucionSunfyreUseCase = iniciarEjecucionSunfyreUseCase;
        this.cancelarSunfyreUseCase = cancelarSunfyreUseCase;
        this.reprogramarSunfyreUseCase = reprogramarSunfyreUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<SunfyreDetalleResponse> crear(@Valid @RequestBody CrearSunfyreRequest request,
                                                        JwtAuthenticationToken token) {
        Sunfyre sunfyre = crearSunfyreUseCase.ejecutar(
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
                .buildAndExpand(sunfyre.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDetalleResponse(sunfyre));
    }

    @GetMapping
    public PageResponse<SunfyreDetalleResponse> buscar(
            @RequestParam(name = "ciudad", required = false) String ciudad,
            @RequestParam(name = "kastor", required = false) SunfyreKastor kastor,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return PageResponse.desde(
                consultarCatalogoSunfyreUseCase.buscar(ciudad, kastor, page, size),
                mapper::toDetalleResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SunfyreDetalleResponse> obtener(@PathVariable String id,
                                                          JwtAuthenticationToken token) {
        Sunfyre sunfyre = obtenerSunfyreUseCase.ejecutar(id, UsuarioActual.id(token));
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }

    @PutMapping("/{id}/publicar")
    public ResponseEntity<SunfyreDetalleResponse> publicar(@PathVariable String id,
                                                           @Valid @RequestBody PublicarSunfyreRequest request,
                                                           JwtAuthenticationToken token) {
        Sunfyre sunfyre = publicarSunfyreUseCase.ejecutar(UsuarioActual.id(token), id, request.destino());
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<SunfyreDetalleResponse> iniciarEjecucion(@PathVariable String id,
                                                                   JwtAuthenticationToken token) {
        Sunfyre sunfyre = iniciarEjecucionSunfyreUseCase.ejecutar(UsuarioActual.id(token), id);
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<SunfyreDetalleResponse> finalizar(@PathVariable String id,
                                                            JwtAuthenticationToken token) {
        Sunfyre sunfyre = finalizarSunfyreUseCase.ejecutar(UsuarioActual.id(token), id);
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<SunfyreDetalleResponse> cancelar(@PathVariable String id,
                                                           @Valid @RequestBody CancelarSunfyreRequest request,
                                                           JwtAuthenticationToken token) {
        Sunfyre sunfyre = cancelarSunfyreUseCase.ejecutar(id, UsuarioActual.id(token), request.motivo());
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }

    @PutMapping("/{id}/reprogramar")
    public ResponseEntity<SunfyreDetalleResponse> reprogramar(@PathVariable String id,
                                                              @Valid @RequestBody ReprogramarSunfyreRequest request,
                                                              JwtAuthenticationToken token) {
        Sunfyre sunfyre = reprogramarSunfyreUseCase.ejecutar(
                UsuarioActual.id(token), id, new Sari(request.inicio(), request.fin()));
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }
}
