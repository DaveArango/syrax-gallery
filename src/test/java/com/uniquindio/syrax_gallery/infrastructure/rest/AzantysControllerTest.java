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
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;
import com.uniquindio.syrax_gallery.domain.valueobject.Laehurlion;
import java.util.NoSuchElementException;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

    @Test
    void deberiaVerificarIdentidadCuandoDatosValidos() throws Exception {
        // Arrange
        String requestJson = """
                {
                    "iksia": "1234567",
                    "laehurlion": "https://cdn.syrax.com/perfiles/az-001.jpg"
                }
                """;
        Azantys azantysVerificado = Azantys.crear("az-001", "Rhaenyra Targaryen",
                new Kostion("Tatuaje realista"), new Runiapos("rhaenyra@syraxgallery.com"));
        azantysVerificado.verificarIksia(new Iksia("1234567"));
        azantysVerificado.subirLaehurlion(new Laehurlion("https://cdn.syrax.com/perfiles/az-001.jpg"));

        when(verificarIdentidadAzantysUseCase.ejecutar(eq("az-001"), any(), any())).thenReturn(azantysVerificado);
        when(mapper.toDetalleResponse(any())).thenReturn(new AzantysDetalleResponse(
                "az-001", "Rhaenyra Targaryen", "Tatuaje realista", "rhaenyra@syraxgallery.com",
                true, true, true, true));

        // Act & Assert
        mockMvc.perform(put("/api/azantys/az-001/identidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.habilitadoParaPublicar").value(true));
    }

    @Test
    void deberiaRetornar400CuandoFaltaLaFotoDePerfil() throws Exception {
        String requestJson = """
                {
                    "iksia": "1234567"
                }
                """;

        mockMvc.perform(put("/api/azantys/az-001/identidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoLaIksiaNoTieneFormatoValido() throws Exception {
        String requestJson = """
                {
                    "iksia": "12ab",
                    "laehurlion": "https://cdn.syrax.com/perfiles/az-001.jpg"
                }
                """;

        mockMvc.perform(put("/api/azantys/az-001/identidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoLaIdentidadYaFueVerificada() throws Exception {
        String requestJson = """
                {
                    "iksia": "1234567",
                    "laehurlion": "https://cdn.syrax.com/perfiles/az-001.jpg"
                }
                """;
        when(verificarIdentidadAzantysUseCase.ejecutar(eq("az-001"), any(), any()))
                .thenThrow(new ReglaDominioException("La identidad ya fue verificada"));

        mockMvc.perform(put("/api/azantys/az-001/identidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La identidad ya fue verificada"));
    }

    @Test
    void deberiaRetornar404CuandoElAzantysNoExiste() throws Exception {
        String requestJson = """
                {
                    "iksia": "1234567",
                    "laehurlion": "https://cdn.syrax.com/perfiles/x.jpg"
                }
                """;
        when(verificarIdentidadAzantysUseCase.ejecutar(eq("no-existe"), any(), any()))
                .thenThrow(new NoSuchElementException("Azantys no encontrado"));

        mockMvc.perform(put("/api/azantys/no-existe/identidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }
}
