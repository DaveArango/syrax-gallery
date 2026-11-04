package com.uniquindio.syrax_gallery.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SeguridadEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sinTokenDeberiaResponder401() throws Exception {
        mockMvc.perform(get("/api/sunfyre/mis-servicios"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void conUnTokenFalsoDeberiaResponder401() throws Exception {
        mockMvc.perform(get("/api/sunfyre/mis-servicios")
                        .header("Authorization", "Bearer esto-no-es-un-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unZentysNoPuedeCrearSunfyreYDeberiaResponder403() throws Exception {
        mockMvc.perform(post("/api/sunfyre")
                        .with(jwt().jwt(j -> j.claim("uid", "zentys-01"))
                                .authorities(new SimpleGrantedAuthority("ZENTYS")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void unAzantysNoPuedeRevisarIdentidadesYDeberiaResponder403() throws Exception {
        mockMvc.perform(put("/api/azantys/az-002/revision")
                        .with(jwt().jwt(j -> j.claim("uid", "az-001"))
                                .authorities(new SimpleGrantedAuthority("AZANTYS")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"aprobada": true}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void unAzantysPuedeVerSusServiciosYDeberiaResponder200() throws Exception {
        mockMvc.perform(get("/api/sunfyre/mis-servicios")
                        .with(jwt().jwt(j -> j.claim("uid", "az-001"))
                                .authorities(new SimpleGrantedAuthority("AZANTYS"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
