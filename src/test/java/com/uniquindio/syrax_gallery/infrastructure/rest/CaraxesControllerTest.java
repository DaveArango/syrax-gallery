package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.CaraxesDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarCatalogoCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.PublicarCaraxesUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarCaraxesUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.CaraxesMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
    private ConsultarCatalogoCaraxesUseCase consultarCatalogoCaraxesUseCase;

    @MockitoBean
    private CaraxesMapper mapper;


    private Caraxes caraxesSimulado() {
        return Caraxes.crear("id-1",
                "azantys-1",
                new Vala(1200.50, "USD"),
                new Sete("Noche Estrellada"),
                new Jorva("Paisaje al atardecer pintado al oleo"),
                new Urnebion("https://cdn.syrax.com/obras/noche-estrellada.jpg"),
                CaraxesKastor.PINTURA);
    }

    private CaraxesDetalleResponse respuestaSimulada(String kanez) {
        return new CaraxesDetalleResponse("id-1",
                "azantys-1",
                1200.50,
                "USD",
                "Noche Estrellada",
                "Paisaje al atardecer pintado al oleo",
                "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                kanez,
                "PINTURA");
    }

    @Test
    void deberiaCrearCaraxesCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "azantysId": "azantys-1",
                    "monto": 1200.50,
                    "divisa": "USD",
                    "sete": "Noche Estrellada",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;

        when(registrarCaraxesUseCase.ejecutar(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(caraxesSimulado());
        when(mapper.toDetalleResponse(any())).thenReturn(respuestaSimulada("BORRADOR"));

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/caraxes/id-1"))
                .andExpect(jsonPath("$.id").value("id-1"))
                .andExpect(jsonPath("$.monto").value(1200.50))
                .andExpect(jsonPath("$.kanez").value("BORRADOR"))
                .andExpect(jsonPath("$.kastor").value("PINTURA"));

        verify(registrarCaraxesUseCase).ejecutar(any(),
                eq("azantys-1"),
                eq(new Vala(1200.50, "USD")),
                eq(new Sete("Noche Estrellada")),
                eq(new Jorva("Paisaje al atardecer pintado al oleo")),
                eq(new Urnebion("https://cdn.syrax.com/obras/noche-estrellada.jpg")),
                eq(CaraxesKastor.PINTURA));
    }

    @Test
    void deberiaRetornar400CuandoFaltaAzantysId() throws Exception {
        String requestJson = """
                {
                    "monto": 1200.50,
                    "divisa": "USD",
                    "sete": "Noche Estrellada",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoFaltaElMonto() throws Exception {
        String requestJson = """
                {
                    "azantysId": "azantys-1",
                    "divisa": "USD",
                    "sete": "Noche Estrellada",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoElMontoEsNegativo() throws Exception {
        String requestJson = """
                {
                    "azantysId": "azantys-1",
                    "monto": -5,
                    "divisa": "USD",
                    "sete": "Noche Estrellada",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Monto inválido: no puede ser negativo"));
    }

    @Test
    void deberiaRetornar400CuandoLaDivisaNoTieneTresLetras() throws Exception {
        String requestJson = """
                {
                    "azantysId": "azantys-1",
                    "monto": 1200.50,
                    "divisa": "US",
                    "sete": "Noche Estrellada",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La divisa debe ser un código ISO de 3 letras"));
    }

    @Test
    void deberiaRetornar400CuandoElSeteEsMuyCorto() throws Exception {
        String requestJson = """
                {
                    "azantysId": "azantys-1",
                    "monto": 1200.50,
                    "divisa": "USD",
                    "sete": "ab",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", containsString("al menos 3 caracteres")));
    }

    @Test
    void deberiaRetornar400CuandoElJorvaEsMuyCorto() throws Exception {
        String requestJson = """
                {
                    "azantysId": "azantys-1",
                    "monto": 1200.50,
                    "divisa": "USD",
                    "sete": "Noche Estrellada",
                    "jorva": "corta",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", containsString("al menos 10 caracteres")));
    }

    @Test
    void deberiaRetornar400CuandoElKastorNoExiste() throws Exception {
        String requestJson = """
                {
                    "azantysId": "azantys-1",
                    "monto": 1200.50,
                    "divisa": "USD",
                    "sete": "Noche Estrellada",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "XYZ"
                }
                """;

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(jsonPath("$.mensaje", containsString("XYZ")));
    }

    @Test
    void deberiaRetornar404CuandoElAzantysNoExiste() throws Exception {
        String requestJson = """
                {
                    "azantysId": "no-existe",
                    "monto": 1200.50,
                    "divisa": "USD",
                    "sete": "Noche Estrellada",
                    "jorva": "Paisaje al atardecer pintado al oleo",
                    "urnebion": "https://cdn.syrax.com/obras/noche-estrellada.jpg",
                    "kastor": "PINTURA"
                }
                """;
        when(registrarCaraxesUseCase.ejecutar(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new NoSuchElementException("Azantys no encontrado"));

        mockMvc.perform(post("/api/caraxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("El recurso solicitado no existe."));
    }

    // ---------- GET /api/caraxes/{id} ----------

    @Test
    void deberiaObtenerCaraxesCuandoExiste() throws Exception {
        Caraxes caraxes = caraxesSimulado();
        when(obtenerCaraxesUseCase.ejecutar("id-1")).thenReturn(caraxes);
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("BORRADOR"));

        mockMvc.perform(get("/api/caraxes/id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id-1"))
                .andExpect(jsonPath("$.azantysId").value("azantys-1"))
                .andExpect(jsonPath("$.sete").value("Noche Estrellada"))
                .andExpect(jsonPath("$.kanez").value("BORRADOR"));
    }

    @Test
    void deberiaRetornar404CuandoElCaraxesNoExiste() throws Exception {
        when(obtenerCaraxesUseCase.ejecutar("no-existe"))
                .thenThrow(new NoSuchElementException("Caraxes no encontrado"));

        mockMvc.perform(get("/api/caraxes/no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("El recurso solicitado no existe."));
    }

    // ---------- PUT /api/caraxes/{id}/publicar/{kanez} ----------

    @Test
    void deberiaPublicarParaExhibicionCuandoDatosValidos() throws Exception {
        Caraxes caraxes = caraxesSimulado();
        when(publicarCaraxesUseCase.ejecutar("id-1", Kanez.EXHIBICION)).thenReturn(caraxes);
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("EXHIBICION"));

        mockMvc.perform(put("/api/caraxes/id-1/publicar/EXHIBICION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kanez").value("EXHIBICION"));
    }

    @Test
    void deberiaPublicarParaVentaCuandoDatosValidos() throws Exception {
        Caraxes caraxes = caraxesSimulado();
        when(publicarCaraxesUseCase.ejecutar("id-1", Kanez.EN_VENTA)).thenReturn(caraxes);
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("EN_VENTA"));

        mockMvc.perform(put("/api/caraxes/id-1/publicar/EN_VENTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kanez").value("EN_VENTA"));
    }

    @Test
    void deberiaRetornar400CuandoElAzantysNoEstaHabilitadoParaPublicar() throws Exception {
        when(publicarCaraxesUseCase.ejecutar("id-1", Kanez.EN_VENTA))
                .thenThrow(new ReglaDominioException("No puede publicar sin verificar su identidad."));

        mockMvc.perform(put("/api/caraxes/id-1/publicar/EN_VENTA"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("No puede publicar sin verificar su identidad."));
    }

    @Test
    void deberiaRetornar400CuandoElKanezNoExiste() throws Exception {
        mockMvc.perform(put("/api/caraxes/id-1/publicar/XYZ"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar404AlPublicarUnCaraxesQueNoExiste() throws Exception {
        when(publicarCaraxesUseCase.ejecutar("no-existe", Kanez.EXHIBICION))
                .thenThrow(new NoSuchElementException("Caraxes no encontrado"));

        mockMvc.perform(put("/api/caraxes/no-existe/publicar/EXHIBICION"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("El recurso solicitado no existe."));
    }

    //--------------------------------------------------------------

    @Test
    void deberiaBuscarCaraxesPublicadasCuandoHayResultados() throws Exception {
        Caraxes caraxes = caraxesSimulado();
        when(consultarCatalogoCaraxesUseCase.buscar("noche",
                CaraxesKastor.PINTURA,
                0,
                5))
                .thenReturn(new Pagina<>(List.of(caraxes), 1,
                        1,
                        0,
                        5));
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("EXHIBICION"));

        mockMvc.perform(get("/api/caraxes")
                        .param("texto", "noche")
                        .param("kastor", "PINTURA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("id-1"))
                .andExpect(jsonPath("$.content[0].kanez").value("EXHIBICION"))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void deberiaRetornar400CuandoElKastorDelCatalogoNoExiste() throws Exception {
        mockMvc.perform(get("/api/caraxes").param("kastor", "XYZ"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(consultarCatalogoCaraxesUseCase);
    }

    @Test
    void deberiaRetornar400CuandoLaPaginaDelCatalogoEsNegativa() throws Exception {
        when(consultarCatalogoCaraxesUseCase.buscar(null,
                null,
                -1,
                5))
                .thenThrow(new ReglaDominioException("La página no puede ser negativa"));

        mockMvc.perform(get("/api/caraxes").param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La página no puede ser negativa"));
    }
}
