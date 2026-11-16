package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.CrearSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.response.SunfyreDetalleResponse;
import com.uniquindio.syrax_gallery.application.dto.response.PageResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarCatalogoSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarSunfyreDeAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.CrearSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerSunfyreUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.valueobject.Alion;
import com.uniquindio.syrax_gallery.domain.valueobject.Jedar;
import com.uniquindio.syrax_gallery.domain.valueobject.Jorva;
import com.uniquindio.syrax_gallery.domain.valueobject.Sete;
import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Urnebion;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.SunfyreMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sunfyre")
@RequiredArgsConstructor
public class SunfyreController {

    private final CrearSunfyreUseCase crearSunfyreUseCase;
    private final ConsultarCatalogoSunfyreUseCase consultarCatalogoUseCase;
    private final ObtenerSunfyreUseCase obtenerSunfyreUseCase;
    private final ConsultarSunfyreDeAzantysUseCase consultarDeAzantysUseCase;
    private final SunfyreMapper mapper;

    // Azantys: ofrecer un servicio nuevo en el catálogo
    @PostMapping
    public ResponseEntity<SunfyreDetalleResponse> crear(@Valid @RequestBody CrearSunfyreRequest request,
                                                           JwtAuthenticationToken token) {
        Alion alion = new Alion(request.ciudad(), request.pais(),
                request.latitud() == null ? 0 : request.latitud(),
                request.longitud() == null ? 0 : request.longitud());
        Jedar horario = new Jedar(
                EnumSet.copyOf(request.dias()), request.apertura(), request.cierre());

        Sunfyre sunfyre = crearSunfyreUseCase.ejecutar(
                UUID.randomUUID().toString(),
                UsuarioActual.id(token),
                new Sete(request.sete()),
                new Jorva(request.jorva()),
                new Urnebion(request.urnebion()),
                request.kastor(),
                alion,
                new Vala(request.monto(), request.divisa()),
                request.duracionMinutos(),
                horario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sunfyre.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDetalleResponse(sunfyre)); // 201
    }

    // Público: catálogo de servicios, paginado y con filtros
    @GetMapping
    public PageResponse<SunfyreDetalleResponse> buscar(
            @RequestParam(name = "texto", required = false) String texto,
            @RequestParam(name = "kastor", required = false) SunfyreKastor kastor,
            @RequestParam(name = "ciudad", required = false) String ciudad,
            @RequestParam(name = "precioMin", required = false) Double precioMin,
            @RequestParam(name = "precioMax", required = false) Double precioMax,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        return PageResponse.desde(
                consultarCatalogoUseCase.buscar(texto, kastor, ciudad, precioMin, precioMax, page, size),
                mapper::toDetalleResponse);
    }

    // Azantys: los servicios que él mismo ofrece
    @GetMapping("/mis-servicios")
    public List<SunfyreDetalleResponse> misServicios(JwtAuthenticationToken token) {
        return consultarDeAzantysUseCase.ejecutar(UsuarioActual.id(token)).stream()
                .map(mapper::toDetalleResponse)
                .toList();
    }

    // Público: detalle de un servicio
    @GetMapping("/{id}")
    public ResponseEntity<SunfyreDetalleResponse> obtener(@PathVariable String id) {
        return ResponseEntity.ok(mapper.toDetalleResponse(obtenerSunfyreUseCase.ejecutar(id)));
    }
}
