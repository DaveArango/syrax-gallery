package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.request.PublicarSunfyreRequest;
import com.uniquindio.syrax_gallery.application.dto.response.SunfyreDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.PublicarSunfyreUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.SunfyreMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sunfyre")
public class SunfyreController {

    private final PublicarSunfyreUseCase publicarSunfyreUseCase;
    private final SunfyreMapper mapper;

    public SunfyreController(PublicarSunfyreUseCase publicarSunfyreUseCase,
                             SunfyreMapper mapper) {
        this.publicarSunfyreUseCase = publicarSunfyreUseCase;
        this.mapper = mapper;
    }

    @PutMapping("/{id}/publicar")
    public ResponseEntity<SunfyreDetalleResponse> publicar(@PathVariable String id,
                                                           @Valid @RequestBody PublicarSunfyreRequest request) {
        Sunfyre sunfyre = publicarSunfyreUseCase.ejecutar(request.azantysId(), id, request.destino());
        return ResponseEntity.ok(mapper.toDetalleResponse(sunfyre));
    }
}
