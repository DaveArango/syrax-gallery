package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.DohaeragonDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.*;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.DohaeragonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

@WebMvcTest(DohaeragonController.class)
@AutoConfigureMockMvc(addFilters = false)
class DohaeragonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrearDohaeragonUseCase crearDohaeragonUseCase;

    @MockitoBean
    private ObtenerDohaeragonUseCase obtenerDohaeragonUseCase;

    @MockitoBean
    private PublicarDohaeragonUseCase publicarDohaeragonUseCase;

    @MockitoBean
    private IniciarEjecucionDohaeragonUseCase iniciarEjecucionDohaeragonUseCase;

    @MockitoBean
    private FinalizarDohaeragonUseCase finalizarDohaeragonUseCase;

    @MockitoBean
    private CancelarDohaeragonUseCase cancelarDohaeragonUseCase;

    @MockitoBean
    private ReprogramarDohaeragonUseCase reprogramarDohaeragonUseCase;

    @MockitoBean
    private ConsultarCatalogoDohaeragonUseCase consultarCatalogoDohaeragonUseCase;

    @MockitoBean
    private ConsultarDohaeragonDeAzantysUseCase consultarDohaeragonDeAzantysUseCase;

    @MockitoBean
    private DohaeragonMapper mapper;

    private JwtAuthenticationToken token(String uid) {
        Jwt jwt = Jwt.withTokenValue("token-de-prueba")
                .header("alg", "HS256")
                .claim("uid", uid)
                .build();
        return new JwtAuthenticationToken(jwt);
    }

    private Dohaeragon dohaeragonSimulada() {
        return Dohaeragon.solicitar("dh-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
    }

    private DohaeragonDetalleResponse respuestaDe(Dohaeragon dohaeragon, String kanez) {
        return new DohaeragonDetalleResponse(
                dohaeragon.getId(), dohaeragon.getIdAzantys(), dohaeragon.getIdZentys(),
                dohaeragon.getSari().inicio(), dohaeragon.getSari().fin(),
                "ARTE_PIEL", "Armenia", "Colombia",
                150000, "COP", "PENDIENTE", kanez, null);
    }

    // ---------- POST /api/dohaeragon ----------

    @Test
    void deberiaCrearDohaeragonCuandoDatosValidos() throws Exception {
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
        when(crearDohaeragonUseCase.ejecutar(any(), eq("az-001"), eq("ze-001"), any(), any(), any(), any()))
                .thenReturn(dohaeragonSimulada());

        mockMvc.perform(post("/api/dohaeragon")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deberiaRetornar400CuandoFaltaElZentys() throws Exception {
        String requestJson = """
                {
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

        mockMvc.perform(post("/api/dohaeragon")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar422CuandoElFinEsAntesDelInicio() throws Exception {
        String requestJson = """
                {
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

        mockMvc.perform(post("/api/dohaeragon")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(422));
    }

    @Test
    void deberiaRetornar404CuandoElZentysNoExiste() throws Exception {
        String requestJson = """
                {
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
        when(crearDohaeragonUseCase.ejecutar(any(), eq("az-001"), eq("no-existe"), any(), any(), any(), any()))
                .thenThrow(new NoSuchElementException("Zentys no encontrado"));

        mockMvc.perform(post("/api/dohaeragon")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /api/dohaeragon/{id} ----------

    @Test
    void deberiaRetornarElDetalleDeLaDohaeragon() throws Exception {
        Dohaeragon dohaeragon = dohaeragonSimulada();
        when(obtenerDohaeragonUseCase.ejecutar("dh-001", "az-001")).thenReturn(dohaeragon);
        when(mapper.toDetalleResponse(dohaeragon)).thenReturn(respuestaDe(dohaeragon, "BORRADOR"));

        mockMvc.perform(get("/api/dohaeragon/dh-001").principal(token("az-001")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("dh-001"))
                .andExpect(jsonPath("$.ciudad").value("Armenia"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void deberiaRetornar422CuandoUnTerceroConsultaLaDohaeragon() throws Exception {
        when(obtenerDohaeragonUseCase.ejecutar("dh-001", "ze-999"))
                .thenThrow(new ReglaDominioException("Solo el Azantys o el Zentys de la Dohaeragon pueden realizar esta acción"));

        mockMvc.perform(get("/api/dohaeragon/dh-001").principal(token("ze-999")))
                .andExpect(status().is(422));
    }

    @Test
    void deberiaRetornar404CuandoLaDohaeragonNoExiste() throws Exception {
        when(obtenerDohaeragonUseCase.ejecutar("no-existe", "az-001"))
                .thenThrow(new NoSuchElementException("Dohaeragon no encontrada"));

        mockMvc.perform(get("/api/dohaeragon/no-existe").principal(token("az-001")))
                .andExpect(status().isNotFound());
    }

    // ---------- PUT /api/dohaeragon/{id}/publicar ----------

    @Test
    void deberiaPublicarDohaeragonCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "destino": "EXHIBICION"
                }
                """;
        Dohaeragon dohaeragon = dohaeragonSimulada();
        dohaeragon.publicar(Kanez.EXHIBICION);
        when(publicarDohaeragonUseCase.ejecutar("az-001", "dh-001", Kanez.EXHIBICION)).thenReturn(dohaeragon);

        mockMvc.perform(put("/api/dohaeragon/dh-001/publicar")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornar400CuandoFaltaElDestino() throws Exception {
        mockMvc.perform(put("/api/dohaeragon/dh-001/publicar")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar422CuandoElAzantysNoEstaVerificado() throws Exception {
        String requestJson = """
                {
                    "destino": "EN_VENTA"
                }
                """;
        when(publicarDohaeragonUseCase.ejecutar(eq("az-002"), eq("dh-001"), any()))
                .thenThrow(new ReglaDominioException("No puede publicar sin verificar su identidad."));

        mockMvc.perform(put("/api/dohaeragon/dh-001/publicar")
                        .principal(token("az-002"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("No puede publicar sin verificar su identidad."));
    }

    // ---------- GET /api/dohaeragon (catálogo público, sin token) ----------

    @Test
    void deberiaBuscarEnElCatalogoConFiltros() throws Exception {
        Dohaeragon dohaeragon = dohaeragonSimulada();
        dohaeragon.publicar(Kanez.EN_VENTA);
        when(consultarCatalogoDohaeragonUseCase.buscar("armenia", SunfyreKastor.ARTE_PIEL, null, null, 0, 10))                .thenReturn(new Pagina<>(List.of(dohaeragon), 1, 1, 0, 5));
        when(mapper.toDetalleResponse(dohaeragon)).thenReturn(respuestaDe(dohaeragon, "EN_VENTA"));

        mockMvc.perform(get("/api/dohaeragon")
                        .param("ciudad", "armenia")
                        .param("kastor", "ARTE_PIEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("dh-001"))
                .andExpect(jsonPath("$.content[0].kanez").value("EN_VENTA"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void deberiaRetornar400CuandoElKastorNoExiste() throws Exception {
        mockMvc.perform(get("/api/dohaeragon").param("kastor", "PINTURA"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoLaPaginaEsNegativa() throws Exception {
        when(consultarCatalogoDohaeragonUseCase.buscar(null, null, null, null, -1, 10))                .thenThrow(new IllegalArgumentException("La página no puede ser negativa"));

        mockMvc.perform(get("/api/dohaeragon").param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaFiltrarElCatalogoPorRangoDePrecio() throws Exception {
        Dohaeragon dohaeragon = dohaeragonSimulada();
        dohaeragon.publicar(Kanez.EN_VENTA);
        when(consultarCatalogoDohaeragonUseCase.buscar(null, null, 100000.0, 200000.0, 0, 10))
                .thenReturn(new Pagina<>(List.of(dohaeragon), 1, 1, 0, 10));
        when(mapper.toDetalleResponse(dohaeragon)).thenReturn(respuestaDe(dohaeragon, "EN_VENTA"));

        mockMvc.perform(get("/api/dohaeragon")
                        .param("precioMin", "100000")
                        .param("precioMax", "200000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("dh-001"))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void deberiaRetornar400CuandoElPrecioMinimoEsMayorQueElMaximo() throws Exception {
        when(consultarCatalogoDohaeragonUseCase.buscar(null, null, 500000.0, 100000.0, 0, 10))
                .thenThrow(new IllegalArgumentException("El precio mínimo no puede ser mayor que el máximo"));

        mockMvc.perform(get("/api/dohaeragon")
                        .param("precioMin", "500000")
                        .param("precioMax", "100000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El precio mínimo no puede ser mayor que el máximo"));
    }

    // ---------- PUT /api/dohaeragon/{id}/iniciar (sin cuerpo) ----------

    @Test
    void deberiaIniciarEjecucionCuandoDatosValidos() throws Exception {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonSimulada();
        dohaeragon.confirmar();
        dohaeragon.iniciarEjecucion();
        when(iniciarEjecucionDohaeragonUseCase.ejecutar("az-001", "dh-001")).thenReturn(dohaeragon);

        // Act & Assert
        mockMvc.perform(put("/api/dohaeragon/dh-001/iniciar").principal(token("az-001")))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornar422CuandoLaDohaeragonNoEstaAbonada() throws Exception {
        when(iniciarEjecucionDohaeragonUseCase.ejecutar("az-001", "dh-001"))
                .thenThrow(new ReglaDominioException("Solo una Dohaeragon ABONADA puede iniciar ejecución"));

        mockMvc.perform(put("/api/dohaeragon/dh-001/iniciar").principal(token("az-001")))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("Solo una Dohaeragon ABONADA puede iniciar ejecución"));
    }

    @Test
    void deberiaRetornar404CuandoLaDohaeragonNoExisteAlIniciar() throws Exception {
        when(iniciarEjecucionDohaeragonUseCase.ejecutar("az-001", "no-existe"))
                .thenThrow(new NoSuchElementException("Dohaeragon no encontrada"));

        mockMvc.perform(put("/api/dohaeragon/no-existe/iniciar").principal(token("az-001")))
                .andExpect(status().isNotFound());
    }

    // ---------- PUT /api/dohaeragon/{id}/finalizar (sin cuerpo) ----------

    @Test
    void deberiaFinalizarCuandoDatosValidos() throws Exception {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonSimulada();
        dohaeragon.confirmar();
        dohaeragon.iniciarEjecucion();
        dohaeragon.finalizar();
        when(finalizarDohaeragonUseCase.ejecutar("az-001", "dh-001")).thenReturn(dohaeragon);

        // Act & Assert
        mockMvc.perform(put("/api/dohaeragon/dh-001/finalizar").principal(token("az-001")))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornar422CuandoLaDohaeragonNoEstaEnEjecucion() throws Exception {
        when(finalizarDohaeragonUseCase.ejecutar("az-001", "dh-001"))
                .thenThrow(new ReglaDominioException("Solo una Dohaeragon en ejecución puede finalizar"));

        mockMvc.perform(put("/api/dohaeragon/dh-001/finalizar").principal(token("az-001")))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("Solo una Dohaeragon en ejecución puede finalizar"));
    }

    @Test
    void deberiaRetornar422CuandoOtroAzantysIntentaFinalizar() throws Exception {
        when(finalizarDohaeragonUseCase.ejecutar("az-002", "dh-001"))
                .thenThrow(new ReglaDominioException("Solo el Azantys creador puede gestionar esta Dohaeragon"));

        mockMvc.perform(put("/api/dohaeragon/dh-001/finalizar").principal(token("az-002")))
                .andExpect(status().is(422));
    }

    // ---------- PUT /api/dohaeragon/{id}/cancelar ----------

    @Test
    void deberiaCancelarCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "motivo": "El cliente viaja esa semana"
                }
                """;
        Dohaeragon dohaeragon = dohaeragonSimulada();
        dohaeragon.cancelar("El cliente viaja esa semana");
        when(cancelarDohaeragonUseCase.ejecutar("dh-001", "ze-001", "El cliente viaja esa semana")).thenReturn(dohaeragon);

        mockMvc.perform(put("/api/dohaeragon/dh-001/cancelar")
                        .principal(token("ze-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornar400CuandoFaltaElMotivo() throws Exception {
        mockMvc.perform(put("/api/dohaeragon/dh-001/cancelar")
                        .principal(token("ze-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar422CuandoLaDohaeragonYaInicio() throws Exception {
        String requestJson = """
                {
                    "motivo": "Ya no la quiero"
                }
                """;
        when(cancelarDohaeragonUseCase.ejecutar("dh-001", "ze-001", "Ya no la quiero"))
                .thenThrow(new ReglaDominioException("Solo una Dohaeragon PENDIENTE o ABONADA puede cancelarse"));

        mockMvc.perform(put("/api/dohaeragon/dh-001/cancelar")
                        .principal(token("ze-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("Solo una Dohaeragon PENDIENTE o ABONADA puede cancelarse"));
    }

    @Test
    void deberiaRetornar404CuandoLaDohaeragonNoExisteAlCancelar() throws Exception {
        String requestJson = """
                {
                    "motivo": "El cliente viaja esa semana"
                }
                """;
        when(cancelarDohaeragonUseCase.ejecutar("no-existe", "ze-001", "El cliente viaja esa semana"))
                .thenThrow(new NoSuchElementException("Dohaeragon no encontrada"));

        mockMvc.perform(put("/api/dohaeragon/no-existe/cancelar")
                        .principal(token("ze-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    // ---------- PUT /api/dohaeragon/{id}/reprogramar ----------

    @Test
    void deberiaReprogramarCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "inicio": "2030-05-20T10:00:00",
                    "fin": "2030-05-20T12:00:00"
                }
                """;
        Dohaeragon dohaeragon = dohaeragonSimulada();
        dohaeragon.confirmar();
        when(reprogramarDohaeragonUseCase.ejecutar(eq("az-001"), eq("dh-001"), any())).thenReturn(dohaeragon);

        mockMvc.perform(put("/api/dohaeragon/dh-001/reprogramar")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornar400CuandoFaltanLasFechas() throws Exception {
        mockMvc.perform(put("/api/dohaeragon/dh-001/reprogramar")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar422CuandoElNuevoHorarioSeCruza() throws Exception {
        String requestJson = """
                {
                    "inicio": "2030-05-20T10:00:00",
                    "fin": "2030-05-20T12:00:00"
                }
                """;
        when(reprogramarDohaeragonUseCase.ejecutar(eq("az-001"), eq("dh-001"), any()))
                .thenThrow(new ReglaDominioException("El Azantys ya tiene una cita en ese horario"));

        mockMvc.perform(put("/api/dohaeragon/dh-001/reprogramar")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("El Azantys ya tiene una cita en ese horario"));
    }

    @Test
    void deberiaRetornar422CuandoElFinEsAntesDelInicioAlReprogramar() throws Exception {
        String requestJson = """
                {
                    "inicio": "2030-05-20T12:00:00",
                    "fin": "2030-05-20T10:00:00"
                }
                """;

        mockMvc.perform(put("/api/dohaeragon/dh-001/reprogramar")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(422));
    }

    // ---------- GET /api/dohaeragon/mis-servicios (Azantys del token) ----------

    @Test
    void deberiaListarLosServiciosDelAzantys() throws Exception {
        Dohaeragon dohaeragon = dohaeragonSimulada();
        when(consultarDohaeragonDeAzantysUseCase.ejecutar("az-001", DohaeroxJeda.PENDIENTE, 0, 10))
                .thenReturn(new Pagina<>(List.of(dohaeragon), 1, 1, 0, 10));
        when(mapper.toDetalleResponse(dohaeragon)).thenReturn(respuestaDe(dohaeragon, "BORRADOR"));

        mockMvc.perform(get("/api/dohaeragon/mis-servicios")
                        .param("estado", "PENDIENTE")
                        .principal(token("az-001")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("dh-001"))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void deberiaRetornar400CuandoElEstadoNoExisteEnMisServicios() throws Exception {
        mockMvc.perform(get("/api/dohaeragon/mis-servicios")
                        .param("estado", "PAGADO")
                        .principal(token("az-001")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoLaPaginaEsNegativaEnMisServicios() throws Exception {
        when(consultarDohaeragonDeAzantysUseCase.ejecutar("az-001", null, -1, 10))
                .thenThrow(new IllegalArgumentException("La página no puede ser negativa"));

        mockMvc.perform(get("/api/dohaeragon/mis-servicios")
                        .param("page", "-1")
                        .principal(token("az-001")))
                .andExpect(status().isBadRequest());
    }
}