package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.usecase.ObtenerZentysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarZentysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.ZentysMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void deberiaCrearZentysCuandoDatosValidos () throws Exception{
        String requestJson = """
                {
                    "zentysId": "zentys-01",
                    "nombre": "david"
                }
                """;

        Runiapos runiapos = new Runiapos("david@gmail.com");
        Indior indior = new Indior("Soy yo de nuevo");

        Zentys zentysSimulado = Zentys.crear("zentys-01",
                "david",
                runiapos,
                indior);

        when(registrarZentysUseCase.ejecutar(any(), any(), any(), any()))
                .thenReturn(zentysSimulado);

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deberiaRetornar400CuandoFaltaZentysId () throws Exception{
        String requestJson = """
                {
                }
                """;

        mockMvc.perform(post("/api/zentys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
