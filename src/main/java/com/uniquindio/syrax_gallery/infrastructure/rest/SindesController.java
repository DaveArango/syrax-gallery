package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.RealizarSindesRequest;
import com.uniquindio.syrax_gallery.application.dto.response.SindesDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.CancelarSindesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConfirmarSindesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarMisSindesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarSindesDeAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RealizarSindesUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.SindesMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sindes")
@RequiredArgsConstructor
public class SindesController {

    private final RealizarSindesUseCase realizarSindesUseCase;
    private final ConfirmarSindesUseCase confirmarSindesUseCase;
    private final CancelarSindesUseCase cancelarSindesUseCase;
    private final ConsultarMisSindesUseCase consultarMisSindesUseCase;
    private final ConsultarSindesDeAzantysUseCase consultarSindesDeAzantysUseCase;
    private final SindesMapper mapper;

    // Zentys: comprar un Caraxes que está EN_VENTA
    @PostMapping
    public ResponseEntity<SindesDetalleResponse> realizar(@Valid @RequestBody RealizarSindesRequest request,
                                                          JwtAuthenticationToken token) {
        Sindes sindes = realizarSindesUseCase.ejecutar(
                UUID.randomUUID().toString(), UsuarioActual.id(token), request.caraxesId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sindes.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDetalleResponse(sindes)); // 201
    }

    // Azantys: confirmar que recibió el pago, la obra queda VENDIDA
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<SindesDetalleResponse> confirmar(@PathVariable String id,
                                                           JwtAuthenticationToken token) {
        Sindes sindes = confirmarSindesUseCase.ejecutar(id, UsuarioActual.id(token));
        return ResponseEntity.ok(mapper.toDetalleResponse(sindes));
    }

    // Zentys: arrepentirse antes de que el Azantys confirme
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<SindesDetalleResponse> cancelar(@PathVariable String id,
                                                          JwtAuthenticationToken token) {
        Sindes sindes = cancelarSindesUseCase.ejecutar(id, UsuarioActual.id(token));
        return ResponseEntity.ok(mapper.toDetalleResponse(sindes));
    }

    // Zentys: ver sus compras, de la más reciente a la más antigua
    @GetMapping("/mis-compras")
    public List<SindesDetalleResponse> misCompras(JwtAuthenticationToken token) {
        return consultarMisSindesUseCase.ejecutar(UsuarioActual.id(token)).stream()
                .map(mapper::toDetalleResponse)
                .toList();
    }

    // Azantys: las compras de sus obras; las PENDIENTE son las que le falta confirmar
    @GetMapping("/mis-ventas")
    public List<SindesDetalleResponse> misVentas(JwtAuthenticationToken token) {
        return consultarSindesDeAzantysUseCase.ejecutar(UsuarioActual.id(token)).stream()
                .map(mapper::toDetalleResponse)
                .toList();
    }
}
