package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.ZentysDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerZentysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarZentysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.ZentysMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ZentysController.class)
public class ZentysControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarZentysUseCase registrarZentysUseCase;

    @MockitoBean
    private ObtenerZentysUseCase obtenerZentysUseCase;

    @MockitoBean
    private ZentysMapper mapper;

    private Zentys zentysSimulado() {
        return Zentys.crear("zentys-01",
                "David Vivas",
                new Runiapos("david@gmail.com"),
                new Indior("Galeria de arte moderno en Armenia."));
    }

    private ZentysDetalleResponse respuestaSimulada() {
        return new ZentysDetalleResponse("zentys-01",
                "David Vivas",
                "david@gmail.com",
                "Galeria de arte moderno en Armenia.",
                true);
    }

    // ---------- POST /api/zentys ----------

    @Test
    void deberiaCrearZentysCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "nombre": "David Vivas",
                    "runiapos": "david@gmail.com",
                    "indior": "Galeria de arte moderno en Armenia."
                }
                """;
        when(registrarZentysUseCase.ejecutar(any(), any(), any(), any())).thenReturn(zentysSimulado());
        when(mapper.toDetalleResponse(any())).thenReturn(respuestaSimulada());

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/zentys/zentys-01"))
                .andExpect(jsonPath("$.id").value("zentys-01"))
                .andExpect(jsonPath("$.nombre").value("David Vivas"))
                .andExpect(jsonPath("$.direccion").value("david@gmail.com"))
                .andExpect(jsonPath("$.descripcion").value("Galeria de arte moderno en Armenia."))
                .andExpect(jsonPath("$.activo").value(true));

        verify(registrarZentysUseCase).ejecutar(any(),
                eq("David Vivas"),
                eq(new Runiapos("david@gmail.com")),
                eq(new Indior("Galeria de arte moderno en Armenia.")));
    }

    @Test
    void deberiaRetornar400CuandoFaltaElNombre() throws Exception {
        String requestJson = """
                {
                    "runiapos": "david@gmail.com",
                    "indior": "Galeria de arte moderno en Armenia."
                }
                """;

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoFaltaElRuniapos() throws Exception {
        String requestJson = """
                {
                    "nombre": "David Vivas",
                    "indior": "Galeria de arte moderno en Armenia."
                }
                """;

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoFaltaElIndior() throws Exception {
        String requestJson = """
                {
                    "nombre": "David Vivas",
                    "runiapos": "david@gmail.com"
                }
                """;

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoElEmailEsInvalido() throws Exception {
        String requestJson = """
                {
                    "nombre": "David Vivas",
                    "runiapos": "correo-sin-arroba",
                    "indior": "Galeria de arte moderno en Armenia."
                }
                """;

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Email inválido"));
    }

    @Test
    void deberiaRetornar400CuandoElIndiorEsMuyCorto() throws Exception {
        String requestJson = """
                {
                    "nombre": "David Vivas",
                    "runiapos": "david@gmail.com",
                    "indior": "corta"
                }
                """;

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", containsString("10 caracteres")));
    }

    @Test
    void deberiaRetornar400CuandoElIndiorTieneCaracteresNoPermitidos() throws Exception {
        // Arrange
        String requestJson = """
                {
                    "nombre": "David Vivas",
                    "runiapos": "david@gmail.com",
                    "indior": "Descripcion con <etiqueta> no valida"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", containsString("caracteres no permitidos")));
    }

    @Test
    void deberiaRetornar400CuandoElNombreTieneCaracteresEspeciales() throws Exception {
        String requestJson = """
                {
                    "nombre": "David@#$",
                    "runiapos": "david@gmail.com",
                    "indior": "Galeria de arte moderno en Armenia."
                }
                """;

        when(registrarZentysUseCase.ejecutar(any(), any(), any(), any()))
                .thenThrow(new ReglaDominioException("El nombre del Zentys no puede contener caracteres especiales."));

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje")
                        .value("El nombre del Zentys no puede contener caracteres especiales."));
    }

    // ---------- GET /api/zentys/{id} ----------

    @Test
    void deberiaObtenerZentysCuandoExiste() throws Exception {
        Zentys zentys = zentysSimulado();
        when(obtenerZentysUseCase.ejecutar("zentys-01")).thenReturn(zentys);
        when(mapper.toDetalleResponse(zentys)).thenReturn(respuestaSimulada());

        mockMvc.perform(get("/api/zentys/zentys-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("zentys-01"))
                .andExpect(jsonPath("$.nombre").value("David Vivas"))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void deberiaRetornar404CuandoElZentysNoExiste() throws Exception {
        when(obtenerZentysUseCase.ejecutar("no-existe"))
                .thenThrow(new NoSuchElementException("Zentys no encontrado"));

        mockMvc.perform(get("/api/zentys/no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("El recurso solicitado no existe."));
    }
}
