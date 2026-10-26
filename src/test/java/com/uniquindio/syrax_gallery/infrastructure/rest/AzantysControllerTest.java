package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.AzantysDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.ConsultarAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RegistrarAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.RevisarVerificacionAzantysUseCase;
import com.uniquindio.syrax_gallery.application.usecase.VerificarIdentidadAzantysUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.AzantysServicios;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.Iksia;
import com.uniquindio.syrax_gallery.domain.valueobject.Kostion;
import com.uniquindio.syrax_gallery.domain.valueobject.Laehurlion;
import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.AzantysMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AzantysController.class)
@AutoConfigureMockMvc(addFilters = false)
class AzantysControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarAzantysUseCase registrarAzantysUseCase;

    @MockitoBean
    private VerificarIdentidadAzantysUseCase verificarIdentidadAzantysUseCase;

    @MockitoBean
    private ConsultarAzantysUseCase consultarAzantysUseCase;

    @MockitoBean
    private RevisarVerificacionAzantysUseCase revisarVerificacionAzantysUseCase;

    @MockitoBean
    private AzantysMapper mapper;

    private Azantys azantysVerificado() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra Targaryen",
                new Kostion("Tatuaje realista"), new Runiapos("rhaenyra@syraxgallery.com"));
        azantys.verificarIksia(new Iksia("1234567"));
        azantys.subirLaehurlion(new Laehurlion("https://cdn.syrax.com/perfiles/az-001.jpg"));
        return azantys;
    }

    @Test
    void deberiaRegistrarAzantysCuandoDatosValidos() throws Exception {
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
        String requestJson = """
                {
                    "iksia": "1234567",
                    "laehurlion": "https://cdn.syrax.com/perfiles/az-001.jpg"
                }
                """;
        when(verificarIdentidadAzantysUseCase.ejecutar(eq("az-001"), any(), any())).thenReturn(azantysVerificado());
        when(mapper.toDetalleResponse(any())).thenReturn(new AzantysDetalleResponse(
                "az-001", "Rhaenyra Targaryen", "Tatuaje realista", "rhaenyra@syraxgallery.com",
                true, true, true, true));

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


    @Test
    void deberiaBuscarAzantysPaginados() throws Exception {
        when(consultarAzantysUseCase.buscar("tatuaje", 0, 5))
                .thenReturn(new Pagina<>(List.of(azantysVerificado()), 1, 1, 0, 5));

        mockMvc.perform(get("/api/azantys").param("busqueda", "tatuaje"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("az-001"))
                .andExpect(jsonPath("$.content[0].kostion").value("Tatuaje realista"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void deberiaRetornar400CuandoElTamanoDePaginaNoEsValido() throws Exception {
        when(consultarAzantysUseCase.buscar(null, 0, 100))
                .thenThrow(new ReglaDominioException("El tamaño de la página debe estar entre 1 y 50"));

        mockMvc.perform(get("/api/azantys").param("size", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornarElPerfilDeUnAzantys() throws Exception {
        when(consultarAzantysUseCase.obtener("az-001")).thenReturn(azantysVerificado());

        mockMvc.perform(get("/api/azantys/az-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Rhaenyra Targaryen"))
                .andExpect(jsonPath("$.laehurlion").value("https://cdn.syrax.com/perfiles/az-001.jpg"));
    }

    @Test
    void deberiaRetornar404CuandoElPerfilNoEstaDisponible() throws Exception {
        when(consultarAzantysUseCase.obtener("az-002"))
                .thenThrow(new NoSuchElementException("Azantys no encontrado"));

        mockMvc.perform(get("/api/azantys/az-002"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaRetornarElRankingDeMasSolicitados() throws Exception {
        when(consultarAzantysUseCase.masSolicitados(3))
                .thenReturn(List.of(new AzantysServicios("az-001", "Rhaenyra Targaryen", 3L)));

        mockMvc.perform(get("/api/azantys/mas-solicitados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].azantysId").value("az-001"))
                .andExpect(jsonPath("$[0].servicios").value(3));
    }

    // ---------- PUT /api/azantys/{id}/revision (CU-48) ----------

    @Test
    void deberiaAprobarLaIdentidad() throws Exception {
        String requestJson = """
                {
                    "aprobada": true
                }
                """;
        when(revisarVerificacionAzantysUseCase.ejecutar(eq("az-001"), eq(true), any())).thenReturn(azantysVerificado());
        when(mapper.toDetalleResponse(any())).thenReturn(new AzantysDetalleResponse(
                "az-001", "Rhaenyra Targaryen", "Tatuaje realista", "rhaenyra@syraxgallery.com",
                true, true, true, true));

        mockMvc.perform(put("/api/azantys/az-001/revision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.habilitadoParaPublicar").value(true));
    }

    @Test
    void deberiaRetornar400CuandoNoSeIndicaLaDecision() throws Exception {
        mockMvc.perform(put("/api/azantys/az-001/revision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoSeRechazaSinMotivo() throws Exception {
        String requestJson = """
                {
                    "aprobada": false
                }
                """;
        when(revisarVerificacionAzantysUseCase.ejecutar(eq("az-002"), eq(false), any()))
                .thenThrow(new ReglaDominioException("El motivo del rechazo es obligatorio"));

        mockMvc.perform(put("/api/azantys/az-002/revision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El motivo del rechazo es obligatorio"));
    }

    @Test
    void deberiaRetornar404CuandoElAzantysARevisarNoExiste() throws Exception {
        String requestJson = """
                {
                    "aprobada": true
                }
                """;
        when(revisarVerificacionAzantysUseCase.ejecutar(eq("no-existe"), eq(true), any()))
                .thenThrow(new NoSuchElementException("Azantys no encontrado"));

        mockMvc.perform(put("/api/azantys/no-existe/revision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }
}