package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.RegistrarAzantysRequest;
import com.uniquindio.syrax_gallery.application.dto.request.VerificarIdentidadRequest;
import com.uniquindio.syrax_gallery.application.dto.response.AzantysDetalleResponse;
import com.uniquindio.syrax_gallery.application.dto.response.AzantysPerfilResponse;
import com.uniquindio.syrax_gallery.application.dto.response.AzantysRankingResponse;
import com.uniquindio.syrax_gallery.application.dto.response.PageResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.VerificarIdentidadAzantysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Laehurlion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.AzantysMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/azantys")
public class AzantysController {

    private final RegistrarAzantysUseCase registrarAzantysUseCase;
    private final VerificarIdentidadAzantysUseCase verificarIdentidadAzantysUseCase;
    private final ConsultarAzantysUseCase consultarAzantysUseCase;
    private final AzantysMapper mapper;

    public AzantysController(RegistrarAzantysUseCase registrarAzantysUseCase,
                             VerificarIdentidadAzantysUseCase verificarIdentidadAzantysUseCase,
                             ConsultarAzantysUseCase consultarAzantysUseCase,
                             AzantysMapper mapper) {
        this.registrarAzantysUseCase = registrarAzantysUseCase;
        this.verificarIdentidadAzantysUseCase = verificarIdentidadAzantysUseCase;
        this.consultarAzantysUseCase = consultarAzantysUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AzantysDetalleResponse> crear(@Valid @RequestBody RegistrarAzantysRequest request) {
        Kostion kostion = new Kostion(request.kostion());
        Runiapos runiapos = new Runiapos(request.runiapos());
        Azantys azantys = registrarAzantysUseCase.ejecutar(
                UUID.randomUUID().toString(),
                request.nombre(),
                kostion,
                runiapos);

        AzantysDetalleResponse response = mapper.toDetalleResponse(azantys);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(azantys.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}/identidad")
    public ResponseEntity<AzantysDetalleResponse> verificarIdentidad(@PathVariable String id,
                                                                     @Valid @RequestBody VerificarIdentidadRequest request) {
        Azantys azantys = verificarIdentidadAzantysUseCase.ejecutar(
                id,
                new Iksia(request.iksia()),
                new Laehurlion(request.laehurlion()));

        return ResponseEntity.ok(mapper.toDetalleResponse(azantys));
    }

    @GetMapping
    public PageResponse<AzantysPerfilResponse> buscar(
            @RequestParam(name = "busqueda", required = false) String busqueda,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return PageResponse.desde(
                consultarAzantysUseCase.buscar(busqueda, page, size),
                AzantysPerfilResponse::desde);
    }

    @GetMapping("/mas-solicitados")
    public List<AzantysRankingResponse> masSolicitados(
            @RequestParam(name = "limite", defaultValue = "3") int limite) {
        return consultarAzantysUseCase.masSolicitados(limite).stream()
                .map(AzantysRankingResponse::desde)
                .toList();
    }

    @GetMapping("/{id}")
    public AzantysPerfilResponse obtener(@PathVariable("id") String id) {
        return AzantysPerfilResponse.desde(consultarAzantysUseCase.obtener(id));
    }
}