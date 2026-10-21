package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.AzantysDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.VerificarIdentidadAzantysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.AzantysMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AzantysController.class)
class AzantysControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarAzantysUseCase registrarAzantysUseCase;

    @MockitoBean
    private VerificarIdentidadAzantysUseCase verificarIdentidadAzantysUseCase;

    @MockitoBean
    private AzantysMapper mapper;

    @Test
    void deberiaRegistrarAzantysCuandoDatosValidos() throws Exception {
        // Arrange
        String requestJson = """
                {
                    "nombre": "Rhaenyra Targaryen",
                    "kostion": "Tatuaje realista",
                    "runiapos": "rhaenyra@syraxgallery.com"
                }
                """;
        Azantys azantysSimulado = Azantys.crear("az-001", "Rhaenyra Targaryen",
                new Kostion("Tatuaje realista"), new Runiapos("rhaenyra@syraxgallery.com"));
        when(registrarAzantysUseCase.ejecutar(any(), any(), any(), any())).thenReturn(azantysSimulado);
        when(mapper.toDetalleResponse(any())).thenReturn(new AzantysDetalleResponse(
                "az-001", "Rhaenyra Targaryen", "Tatuaje realista", "rhaenyra@syraxgallery.com",
                true, false, false, false));

        // Act & Assert
        mockMvc.perform(post("/api/azantys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.habilitadoParaPublicar").value(false));
    }

    @Test
    void deberiaRetornar400CuandoFaltaElNombre() throws Exception {
        String requestJson = """
                {
                    "kostion": "Tatuaje realista",
                    "runiapos": "rhaenyra@syraxgallery.com"
                }
                """;

        mockMvc.perform(post("/api/azantys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoElCorreoEsInvalido() throws Exception {
        String requestJson = """
                {
                    "nombre": "Rhaenyra Targaryen",
                    "kostion": "Tatuaje realista",
                    "runiapos": "correo-sin-arroba"
                }
                """;

        mockMvc.perform(post("/api/azantys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
