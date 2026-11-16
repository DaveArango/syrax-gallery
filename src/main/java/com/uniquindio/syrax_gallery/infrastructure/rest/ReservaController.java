package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.ReservarSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.response.DohaeragonDetalleResponse;
import com.uniquindio.syrax_gallery.application.dto.response.VentanaResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarDisponibilidadSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarDohaeragonDeZentysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ReservarSunfyreUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.DohaeragonMapper;
import com.uniquindio.syrax_gallery.infrastructure.security.UsuarioActual;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Reservar un servicio: une a la Sunfyre (lo que se ofrece) con su Dohaeragon (la cita del cliente).
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservaController {

    private final ConsultarDisponibilidadSunfyreUseCase consultarDisponibilidadUseCase;
    private final ReservarSunfyreUseCase reservarSunfyreUseCase;
    private final ConsultarDohaeragonDeZentysUseCase consultarDeZentysUseCase;
    private final DohaeragonMapper mapper;

    // Público: horas libres de un servicio en una fecha. Ejemplo: ?fecha=2027-03-15
    @GetMapping("/sunfyre/{id}/disponibilidad")
    public List<VentanaResponse> disponibilidad(
            @PathVariable String id,
            @RequestParam(name = "fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return consultarDisponibilidadUseCase.ejecutar(id, fecha).stream()
                .map(sari -> new VentanaResponse(sari.inicio(), sari.fin()))
                .toList();
    }

    // Zentys: reservar el servicio en una de sus horas libres
    @PostMapping("/sunfyre/{id}/reservas")
    public ResponseEntity<DohaeragonDetalleResponse> reservar(@PathVariable String id,
                                                              @Valid @RequestBody ReservarSunfyreRequest request,
                                                              JwtAuthenticationToken token) {
        Dohaeragon dohaeragon = reservarSunfyreUseCase.ejecutar(
                UUID.randomUUID().toString(), UsuarioActual.id(token), id, request.inicio());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDetalleResponse(dohaeragon)); // 201
    }

    // Zentys: sus reservas, de la más próxima a la más lejana
    @GetMapping("/dohaeragon/mis-reservas")
    public List<DohaeragonDetalleResponse> misReservas(JwtAuthenticationToken token) {
        return consultarDeZentysUseCase.ejecutar(UsuarioActual.id(token)).stream()
                .map(mapper::toDetalleResponse)
                .toList();
    }
}
