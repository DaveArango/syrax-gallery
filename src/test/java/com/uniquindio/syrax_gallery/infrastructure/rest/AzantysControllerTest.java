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
import com.uniquindio.syrax_gallery.infrastructure.security.CredencialesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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
    private CredencialesService credencialesService;

    @MockitoBean
    private AzantysMapper mapper;

    // Simula el token de quien hace la petición (uid = id del Azantys)
    private JwtAuthenticationToken token(String uid) {
        Jwt jwt = Jwt.withTokenValue("token-de-prueba")
                .header("alg", "HS256")
                .claim("uid", uid)
                .build();
        return new JwtAuthenticationToken(jwt);
    }

    private Azantys azantysVerificado() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra Targaryen",
                new Kostion("Tatuaje realista"), new Runiapos("rhaenyra@syraxgallery.com"));
        azantys.verificarIksia(new Iksia("1234567"));
        azantys.subirLaehurlion(new Laehurlion("https://cdn.syrax.com/perfiles/az-001.jpg"));
        return azantys;
    }

    // ---------- POST /api/azantys (público) ----------

    @Test
    void deberiaRegistrarAzantysCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "nombre": "Rhaenyra Targaryen",
                    "kostion": "Tatuaje realista",
                    "runiapos": "rhaenyra@syraxgallery.com",
                    "clave": "rhaenyra123"
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
                    "runiapos": "rhaenyra@syraxgallery.com",
                    "clave": "rhaenyra123"
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
                    "runiapos": "correo-sin-arroba",
                    "clave": "rhaenyra123"
                }
                """;

        mockMvc.perform(post("/api/azantys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaRetornar400CuandoFaltaLaClave() throws Exception {
        String requestJson = """
                {
                    "nombre": "Rhaenyra Targaryen",
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
    void deberiaRetornar400CuandoElCorreoYaTieneCuenta() throws Exception {
        String requestJson = """
                {
                    "nombre": "Rhaenyra Targaryen",
                    "kostion": "Tatuaje realista",
                    "runiapos": "rhaenyra@syraxgallery.com",
                    "clave": "rhaenyra123"
                }
                """;
        doThrow(new ReglaDominioException("Ya existe una cuenta con ese correo"))
                .when(credencialesService).validarCorreoDisponible("rhaenyra@syraxgallery.com");

        mockMvc.perform(post("/api/azantys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Ya existe una cuenta con ese correo"));
    }

    // ---------- PUT /api/azantys/{id}/identidad ----------

    @Test
    void deberiaVerificarIdentidadCuandoDatosValidos() throws Exception {
        String requestJson = """
                {
                    "iksia": "1234567",
                    "laehurlion": "https://cdn.syrax.com/perfiles/az-001.jpg"
                }
                """;
        when(verificarIdentidadAzantysUseCase.ejecutar(eq("az-001"), eq("az-001"), any(), any())).thenReturn(azantysVerificado());
        when(mapper.toDetalleResponse(any())).thenReturn(new AzantysDetalleResponse(
                "az-001", "Rhaenyra Targaryen", "Tatuaje realista", "rhaenyra@syraxgallery.com",
                true, true, true, true));

        mockMvc.perform(put("/api/azantys/az-001/identidad")
                        .principal(token("az-001"))
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
                        .principal(token("az-001"))
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
                        .principal(token("az-001"))
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
        when(verificarIdentidadAzantysUseCase.ejecutar(eq("az-001"), eq("az-001"), any(), any()))
                .thenThrow(new ReglaDominioException("La identidad ya fue verificada"));

        mockMvc.perform(put("/api/azantys/az-001/identidad")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La identidad ya fue verificada"));
    }

    @Test
    void deberiaRetornar400CuandoOtroAzantysCargaLaIdentidad() throws Exception {
        String requestJson = """
                {
                    "iksia": "1234567",
                    "laehurlion": "https://cdn.syrax.com/perfiles/az-001.jpg"
                }
                """;
        when(verificarIdentidadAzantysUseCase.ejecutar(eq("az-001"), eq("az-002"), any(), any()))
                .thenThrow(new ReglaDominioException("Solo el propio Azantys puede cargar su identidad"));

        mockMvc.perform(put("/api/azantys/az-001/identidad")
                        .principal(token("az-002"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Solo el propio Azantys puede cargar su identidad"));
    }

    @Test
    void deberiaRetornar404CuandoElAzantysNoExiste() throws Exception {
        String requestJson = """
                {
                    "iksia": "1234567",
                    "laehurlion": "https://cdn.syrax.com/perfiles/x.jpg"
                }
                """;
        when(verificarIdentidadAzantysUseCase.ejecutar(eq("no-existe"), eq("az-001"), any(), any()))
                .thenThrow(new NoSuchElementException("Azantys no encontrado"));

        mockMvc.perform(put("/api/azantys/no-existe/identidad")
                        .principal(token("az-001"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }
}