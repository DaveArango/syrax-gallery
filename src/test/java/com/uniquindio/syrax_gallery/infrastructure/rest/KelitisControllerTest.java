package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.KelitisDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.AprobarEjecucionKelitisUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConstituirKelitisUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerKelitisUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.KelitisMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KelitisController.class)
class KelitisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConstituirKelitisUseCase constituirKelitisUseCase;

    @MockitoBean
    private ObtenerKelitisUseCase obtenerKelitisUseCase;

    @MockitoBean
    private AprobarEjecucionKelitisUseCase aprobarEjecucionUseCase;

    @MockitoBean
    private KelitisMapper mapper;

    private Kelitis kelitisSimulado() {
        return Kelitis.realizar("kl-001", "sf-001", "az-001", "ze-001", new Vala(150000, "COP"));
    }

    private KelitisDetalleResponse respuesta(String gelior) {
        return new KelitisDetalleResponse("kl-001", "sf-001", "az-001", "ze-001", 150000, "COP", gelior);
    }

    @Test
    void deberiaConstituirKelitisCuandoDatosValidos() throws Exception {
        // Arrange
        String requestJson = """
                {
                    "sunfyreId": "sf-001"
                }
                """;
        when(constituirKelitisUseCase.ejecutar(any(), eq("sf-001"))).thenReturn(kelitisSimulado());
        when(mapper.toDetalleResponse(any())).thenReturn(respuesta("RETENIDO"));

        // Act & Assert
        mockMvc.perform(post("/api/kelitis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.gelior").value("RETENIDO"));
    }

    @Test
    void deberiaRetornar400CuandoFaltaLaSunfyre() throws Exception {
        mockMvc.perform(post("/api/kelitis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoLaSunfyreYaTieneKelitis() throws Exception {
        String requestJson = """
                {
                    "sunfyreId": "sf-001"
                }
                """;
        when(constituirKelitisUseCase.ejecutar(any(), eq("sf-001")))
                .thenThrow(new ReglaDominioException("La Sunfyre ya tiene un Kelitis constituido"));

        mockMvc.perform(post("/api/kelitis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La Sunfyre ya tiene un Kelitis constituido"));
    }

    @Test
    void deberiaRetornarElDetalleDelKelitis() throws Exception {
        Kelitis kelitis = kelitisSimulado();
        when(obtenerKelitisUseCase.ejecutar("kl-001")).thenReturn(kelitis);
        when(mapper.toDetalleResponse(kelitis)).thenReturn(respuesta("RETENIDO"));

        mockMvc.perform(get("/api/kelitis/kl-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSunfyre").value("sf-001"))
                .andExpect(jsonPath("$.monto").value(150000.0));
    }

    @Test
    void deberiaRetornar404CuandoElKelitisNoExiste() throws Exception {
        when(obtenerKelitisUseCase.ejecutar("no-existe"))
                .thenThrow(new NoSuchElementException("Kelitis no encontrado"));

        mockMvc.perform(get("/api/kelitis/no-existe"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaAprobarLaEjecucion() throws Exception {
        // Arrange
        String requestJson = """
                {
                    "zentysId": "ze-001"
                }
                """;
        Kelitis kelitis = kelitisSimulado();
        kelitis.liberarPago();
        when(aprobarEjecucionUseCase.ejecutar("kl-001", "ze-001")).thenReturn(kelitis);
        when(mapper.toDetalleResponse(kelitis)).thenReturn(respuesta("LIBERADO"));

        // Act & Assert
        mockMvc.perform(put("/api/kelitis/kl-001/aprobar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gelior").value("LIBERADO"));
    }

    @Test
    void deberiaRetornar400CuandoElServicioNoSeHaEjecutado() throws Exception {
        String requestJson = """
                {
                    "zentysId": "ze-001"
                }
                """;
        when(aprobarEjecucionUseCase.ejecutar("kl-001", "ze-001"))
                .thenThrow(new ReglaDominioException("Solo se puede aprobar un servicio ya ejecutado"));

        mockMvc.perform(put("/api/kelitis/kl-001/aprobar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
