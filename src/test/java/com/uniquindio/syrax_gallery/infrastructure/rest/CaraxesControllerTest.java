package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.dto.response.CaraxesDetalleResponse;
import com.uniquindio.syrax_gallery.application.usecase.*;
import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.CaraxesMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
@AutoConfigureMockMvc(addFilters = false)
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
    private RetirarCaraxesUseCase retirarCaraxesUseCase;

    @MockitoBean
    private VenderCaraxesUseCase venderCaraxesUseCase;

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

    // ---------- PUT /api/caraxes/{id}/publicar ----------

    @Test
    void deberiaPublicarParaExhibicionCuandoElAzantysEsElCreador() throws Exception {
        // Arrange
        Caraxes caraxes = caraxesSimulado();
        when(publicarCaraxesUseCase.ejecutar("az-001", "id-1", Kanez.EXHIBICION)).thenReturn(caraxes);
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("EXHIBICION"));

        // Act & Assert
        mockMvc.perform(put("/api/caraxes/id-1/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-001", "destino": "EXHIBICION"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kanez").value("EXHIBICION"));
    }

    @Test
    void deberiaPublicarEnVentaCuandoElAzantysEsElCreador() throws Exception {
        Caraxes caraxes = caraxesSimulado();
        when(publicarCaraxesUseCase.ejecutar("az-001", "id-1", Kanez.EN_VENTA)).thenReturn(caraxes);
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("EN_VENTA"));

        mockMvc.perform(put("/api/caraxes/id-1/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-001", "destino": "EN_VENTA"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kanez").value("EN_VENTA"));
    }

    @Test
    void deberiaRetornar400CuandoQuienPublicaNoEsElCreador() throws Exception {
        when(publicarCaraxesUseCase.ejecutar("az-999", "id-1", Kanez.EXHIBICION))
                .thenThrow(new ReglaDominioException("Solo el Azantys creador puede gestionar este Caraxes"));

        mockMvc.perform(put("/api/caraxes/id-1/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-999", "destino": "EXHIBICION"}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Solo el Azantys creador puede gestionar este Caraxes"));
    }

    @Test
    void deberiaRetornar400CuandoElAzantysNoEstaHabilitado() throws Exception {
        // Arrange
        when(publicarCaraxesUseCase.ejecutar("az-001", "id-1", Kanez.EN_VENTA))
                .thenThrow(new ReglaDominioException("No puede publicar sin verificar su identidad."));

        // Act & Assert
        mockMvc.perform(put("/api/caraxes/id-1/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-001", "destino": "EN_VENTA"}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("No puede publicar sin verificar su identidad."));
    }

    @Test
    void deberiaRetornar400CuandoFaltaElAzantysIdOElDestino() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/caraxes/id-1/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(publicarCaraxesUseCase);
    }

    @Test
    void deberiaRetornar400CuandoElDestinoNoExiste() throws Exception {
        mockMvc.perform(put("/api/caraxes/id-1/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-001", "destino": "XYZ"}
                            """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(publicarCaraxesUseCase);
    }

    @Test
    void deberiaRetornar404CuandoElCaraxesNoExiste() throws Exception {
        when(publicarCaraxesUseCase.ejecutar("az-001", "no-existe", Kanez.EXHIBICION))
                .thenThrow(new NoSuchElementException("Caraxes no encontrado"));

        mockMvc.perform(put("/api/caraxes/no-existe/publicar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-001", "destino": "EXHIBICION"}
                            """))
                .andExpect(status().isNotFound());
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

    //----------------------------------------------------

    @Test
    void deberiaRetirarCaraxesCuandoElAzantysEsElCreador() throws Exception {
        Caraxes caraxes = caraxesSimulado();
        when(retirarCaraxesUseCase.ejecutar("az-001",
                "id-1")).thenReturn(caraxes);
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("RETIRADO"));

        mockMvc.perform(put("/api/caraxes/id-1/retirar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-001"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kanez").value("RETIRADO"));
    }

    @Test
    void deberiaRetornar400CuandoRetirarNoRecibeAzantysId() throws Exception {
        mockMvc.perform(put("/api/caraxes/id-1/retirar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(retirarCaraxesUseCase);
    }

    @Test
    void deberiaRetornar400CuandoQuienRetiraNoEsElCreador() throws Exception {
        when(retirarCaraxesUseCase.ejecutar("az-999",
                "id-1"))
                .thenThrow(new ReglaDominioException("Solo el Azantys creador puede gestionar este Caraxes"));

        mockMvc.perform(put("/api/caraxes/id-1/retirar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"azantysId": "az-999"}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Solo el Azantys creador puede gestionar este Caraxes"));
    }

    @Test
    void deberiaVenderCaraxesCuandoEstaEnVenta() throws Exception {
        Caraxes caraxes = caraxesSimulado();
        when(venderCaraxesUseCase.ejecutar("zentys-01",
                "id-1")).thenReturn(caraxes);
        when(mapper.toDetalleResponse(caraxes)).thenReturn(respuestaSimulada("VENDIDO"));

        mockMvc.perform(put("/api/caraxes/id-1/vender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"zentysId": "zentys-01"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kanez").value("VENDIDO"));
    }

    @Test
    void deberiaRetornar400CuandoElCaraxesNoEstaEnVenta() throws Exception {
        when(venderCaraxesUseCase.ejecutar("zentys-01",
                "id-1"))
                .thenThrow(new ReglaDominioException("Solo un Caraxes EN_VENTA puede marcarse como vendido"));

        mockMvc.perform(put("/api/caraxes/id-1/vender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"zentysId": "zentys-01"}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Solo un Caraxes EN_VENTA puede marcarse como vendido"));
    }

    @Test
    void deberiaRetornar404CuandoElZentysNoExiste() throws Exception {
        when(venderCaraxesUseCase.ejecutar("zentys-99",
                "id-1"))
                .thenThrow(new NoSuchElementException("Zentys no encontrado"));

        mockMvc.perform(put("/api/caraxes/id-1/vender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"zentysId": "zentys-99"}
                            """))
                .andExpect(status().isNotFound());
    }
}
