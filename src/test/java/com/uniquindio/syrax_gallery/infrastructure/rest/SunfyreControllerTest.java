package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.SunfyreDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarCatalogoSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.CrearSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerSunfyreUseCase;
import com.uniquindio.syrax_gallery.application.usecase.PublicarSunfyreUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.SunfyreMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SunfyreController.class)
class SunfyreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrearSunfyreUseCase crearSunfyreUseCase;

    @MockitoBean
    private ObtenerSunfyreUseCase obtenerSunfyreUseCase;

    @MockitoBean
    private PublicarSunfyreUseCase publicarSunfyreUseCase;

    @MockitoBean
    private ConsultarCatalogoSunfyreUseCase consultarCatalogoSunfyreUseCase;

    @MockitoBean
    private SunfyreMapper mapper;

    private Sunfyre sunfyreSimulada() {
        return Sunfyre.solicitar("sf-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
    }

    private SunfyreDetalleResponse respuestaDe(Sunfyre sunfyre, String kanez) {
        return new SunfyreDetalleResponse(
                sunfyre.getId(), sunfyre.getIdAzantys(), sunfyre.getIdZentys(),
                sunfyre.getSari().inicio(), sunfyre.getSari().fin(),
                "ARTE_PIEL", "Armenia", "Colombia",
                150000, "COP", "PENDIENTE", kanez);
    }

    // ---------- POST /api/sunfyre ----------

    @Test
    void deberiaCrearSunfyreCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "azantysId": "az-001",
                    "zentysId": "ze-001",
                    "inicio": "2030-03-10T10:00:00",
                    "fin": "2030-03-10T13:00:00",
                    "kastor": "ARTE_PIEL",
                    "ciudad": "Armenia",
                    "pais": "Colombia",
                    "latitud": 4.5339,
                    "longitud": -75.6811,
                    "monto": 150000,
                    "divisa": "COP"
                }
                """;
        when(crearSunfyreUseCase.ejecutar(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(sunfyreSimulada());

        mockMvc.perform(post("/api/sunfyre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deberiaRetornar400CuandoFaltaElAzantys() throws Exception {
        String requestJson = """
                {
                    "zentysId": "ze-001",
                    "inicio": "2030-03-10T10:00:00",
                    "fin": "2030-03-10T13:00:00",
                    "kastor": "ARTE_PIEL",
                    "ciudad": "Armenia",
                    "pais": "Colombia",
                    "latitud": 4.5339,
                    "longitud": -75.6811,
                    "monto": 150000,
                    "divisa": "COP"
                }
                """;

        mockMvc.perform(post("/api/sunfyre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoElFinEsAntesDelInicio() throws Exception {
        String requestJson = """
                {
                    "azantysId": "az-001",
                    "zentysId": "ze-001",
                    "inicio": "2030-03-10T13:00:00",
                    "fin": "2030-03-10T10:00:00",
                    "kastor": "ARTE_PIEL",
                    "ciudad": "Armenia",
                    "pais": "Colombia",
                    "latitud": 4.5339,
                    "longitud": -75.6811,
                    "monto": 150000,
                    "divisa": "COP"
                }
                """;

        mockMvc.perform(post("/api/sunfyre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar404CuandoElZentysNoExiste() throws Exception {
        String requestJson = """
                {
                    "azantysId": "az-001",
                    "zentysId": "no-existe",
                    "inicio": "2030-03-10T10:00:00",
                    "fin": "2030-03-10T13:00:00",
                    "kastor": "ARTE_PIEL",
                    "ciudad": "Armenia",
                    "pais": "Colombia",
                    "latitud": 4.5339,
                    "longitud": -75.6811,
                    "monto": 150000,
                    "divisa": "COP"
                }
                """;
        when(crearSunfyreUseCase.ejecutar(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new NoSuchElementException("Zentys no encontrado"));

        mockMvc.perform(post("/api/sunfyre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /api/sunfyre/{id} ----------

    @Test
    void deberiaRetornarElDetalleDeLaSunfyre() throws Exception {
        Sunfyre sunfyre = sunfyreSimulada();
        when(obtenerSunfyreUseCase.ejecutar("sf-001")).thenReturn(sunfyre);
        when(mapper.toDetalleResponse(sunfyre)).thenReturn(respuestaDe(sunfyre, "BORRADOR"));

        mockMvc.perform(get("/api/sunfyre/sf-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("sf-001"))
                .andExpect(jsonPath("$.ciudad").value("Armenia"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void deberiaRetornar404CuandoLaSunfyreNoExiste() throws Exception {
        when(obtenerSunfyreUseCase.ejecutar("no-existe"))
                .thenThrow(new NoSuchElementException("Sunfyre no encontrada"));

        mockMvc.perform(get("/api/sunfyre/no-existe"))
                .andExpect(status().isNotFound());
    }

    // ---------- PUT /api/sunfyre/{id}/publicar ----------

    @Test
    void deberiaPublicarSunfyreCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "azantysId": "az-001",
                    "destino": "EXHIBICION"
                }
                """;
        Sunfyre sunfyre = sunfyreSimulada();
        sunfyre.publicar(Kanez.EXHIBICION);
        when(publicarSunfyreUseCase.ejecutar("az-001", "sf-001", Kanez.EXHIBICION)).thenReturn(sunfyre);

        mockMvc.perform(put("/api/sunfyre/sf-001/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornar400CuandoFaltaElDestino() throws Exception {
        String requestJson = """
                {
                    "azantysId": "az-001"
                }
                """;

        mockMvc.perform(put("/api/sunfyre/sf-001/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoElAzantysNoEstaVerificado() throws Exception {
        String requestJson = """
                {
                    "azantysId": "az-002",
                    "destino": "EN_VENTA"
                }
                """;
        when(publicarSunfyreUseCase.ejecutar(eq("az-002"), eq("sf-001"), any()))
                .thenThrow(new ReglaDominioException("No puede publicar sin verificar su identidad."));

        mockMvc.perform(put("/api/sunfyre/sf-001/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("No puede publicar sin verificar su identidad."));
    }

    @Test
    void deberiaBuscarEnElCatalogoConFiltros() throws Exception {
        Sunfyre sunfyre = sunfyreSimulada();
        sunfyre.publicar(Kanez.EN_VENTA);
        when(consultarCatalogoSunfyreUseCase.buscar("armenia", SunfyreKastor.ARTE_PIEL, 0, 5))
                .thenReturn(new Pagina<>(List.of(sunfyre), 1, 1, 0, 5));
        when(mapper.toDetalleResponse(sunfyre)).thenReturn(respuestaDe(sunfyre, "EN_VENTA"));

        mockMvc.perform(get("/api/sunfyre")
                        .param("ciudad", "armenia")
                        .param("kastor", "ARTE_PIEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("sf-001"))
                .andExpect(jsonPath("$.content[0].kanez").value("EN_VENTA"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void deberiaRetornar400CuandoElKastorNoExiste() throws Exception {
        mockMvc.perform(get("/api/sunfyre").param("kastor", "PINTURA"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoLaPaginaEsNegativa() throws Exception {
        when(consultarCatalogoSunfyreUseCase.buscar(null, null, -1, 5))
                .thenThrow(new ReglaDominioException("La página no puede ser negativa"));

        mockMvc.perform(get("/api/sunfyre").param("page", "-1"))
                .andExpect(status().isBadRequest());
    }
}