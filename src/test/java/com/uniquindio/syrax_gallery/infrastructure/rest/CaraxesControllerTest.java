package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.usecase.ObtenerCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.PublicarCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarCaraxesUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.CaraxesMapper;
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

@WebMvcTest(CaraxesController.class)
public class CaraxesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ObtenerCaraxesUseCase obtenerCaraxesUseCase;

    @MockitoBean
    private RegistrarCaraxesUseCase registrarCaraxesUseCase;

    @MockitoBean
    private PublicarCaraxesUseCase publicarCaraxesUseCase;

    @MockitoBean
    private CaraxesMapper mapper;

    @Test
    void deberiaCrearCaraxesCuandoDatosValidos () throws Exception{
        String requestJson = """
                {
                    "azantysId": "azantys-1"
                }
                """;

        Vala vala = new Vala(200000, "COP");
        Sete sete = new Sete("La noche estrellada de Van Gogh");
        Jorva jorva = new Jorva("Magnifica obra física");
        Urnebion urnebion = new Urnebion("https://cdn.syrax.com/obras/la-noche-estrellada-van.jpg");
        CaraxesKastor kastor = CaraxesKastor.PINTURA;

        Caraxes caraxesSimulado = Caraxes.crear("id-1",
                "azantys-1",
                vala,
                sete,
                jorva,
                urnebion,
                kastor);

        when(registrarCaraxesUseCase.ejecutar(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(caraxesSimulado);

        mockMvc.perform(post("/api/caraxes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deberiaRetornar400CuandoFaltaAzantysId () throws Exception{
        String requestJson = """
                {
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

}
