package com.uniquindio.syrax_gallery.integracion;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.DohaeroxJeda;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FlujoSunfyreIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SunfyreRepository sunfyreRepository;

    @Autowired
    private KelitisRepository kelitisRepository;

    @Test
    void unAzantysDeberiaCrearUnaSunfyreQueNacePendiente() throws Exception {
        String cuerpo = """
                {
                  "zentysId": "zentys-01",
                  "inicio": "2028-02-10T10:00:00",
                  "fin": "2028-02-10T12:00:00",
                  "kastor": "ARTE_PIEL",
                  "ciudad": "Armenia",
                  "pais": "Colombia",
                  "latitud": 4.5339,
                  "longitud": -75.6811,
                  "monto": 250000,
                  "divisa": "COP"
                }
                """;

        mockMvc.perform(post("/api/sunfyre")
                        .with(jwt().jwt(j -> j.claim("uid", "az-001"))
                                .authorities(new SimpleGrantedAuthority("AZANTYS")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.idAzantys").value("az-001"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.kanez").value("BORRADOR"));
    }

    @Test
    void unZentysDeberiaPagarUnaSunfyreYQuedarElDineroRetenido() throws Exception {
        String cuerpo = """
                {"sunfyreId": "sf-007"}
                """;

        mockMvc.perform(post("/api/kelitis")
                        .with(jwt().jwt(j -> j.claim("uid", "zentys-01"))
                                .authorities(new SimpleGrantedAuthority("ZENTYS")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idSunfyre").value("sf-007"))
                .andExpect(jsonPath("$.gelior").value("RETENIDO"));

        Sunfyre sunfyre = sunfyreRepository.obtener("sf-007").orElseThrow();
        assertEquals(DohaeroxJeda.ABONADO, sunfyre.getDohaeroxJeda());
        assertTrue(kelitisRepository.existePorSunfyre("sf-007"));
    }

    @Test
    void cancelarUnaSunfyreCompletadaDeberiaResponder422YNoCambiarNada() throws Exception {
        String cuerpo = """
                {"motivo": "Ya no lo quiero"}
                """;

        mockMvc.perform(put("/api/sunfyre/sf-004/cancelar")
                        .with(jwt().jwt(j -> j.claim("uid", "zentys-02"))
                                .authorities(new SimpleGrantedAuthority("ZENTYS")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("Solo una Sunfyre PENDIENTE o ABONADA puede cancelarse"));

        Sunfyre sunfyre = sunfyreRepository.obtener("sf-004").orElseThrow();
        assertEquals(DohaeroxJeda.COMPLETADO, sunfyre.getDohaeroxJeda());
    }
}
