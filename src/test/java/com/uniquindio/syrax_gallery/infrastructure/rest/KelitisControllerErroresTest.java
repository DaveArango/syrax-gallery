package com.uniquindio.syrax_gallery.infrastructure.rest;

import com.uniquindio.syrax_gallery.application.usecase.AprobarEjecucionKelitisUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ConstituirKelitisUseCase;
import com.uniquindio.syrax_gallery.application.usecase.ObtenerKelitisUseCase;
import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.rest.mapper.KelitisMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KelitisController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AprobarEjecucionKelitisUseCase.class)
class KelitisControllerErroresTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KelitisRepository kelitisRepository;

    @MockitoBean
    private SunfyreRepository sunfyreRepository;

    @MockitoBean
    private ConstituirKelitisUseCase constituirKelitisUseCase;

    @MockitoBean
    private ObtenerKelitisUseCase obtenerKelitisUseCase;

    @MockitoBean
    private KelitisMapper mapper;

    private JwtAuthenticationToken token(String uid) {
        Jwt jwt = Jwt.withTokenValue("token-de-prueba")
                .header("alg", "HS256")
                .claim("uid", uid)
                .build();
        return new JwtAuthenticationToken(jwt);
    }

    private Kelitis kelitisRetenido() {
        return Kelitis.realizar("kl-001", "sf-001", "az-001", "ze-001", new Vala(150000, "COP"));
    }

    private Sunfyre sunfyreAbonada() {
        Sunfyre sunfyre = Sunfyre.solicitar("sf-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
        sunfyre.confirmar();
        return sunfyre;
    }

    @Test
    void deberiaResponder404CuandoElKelitisNoExisteYNoGuardarNada() throws Exception {
        when(kelitisRepository.obtener("no-existe")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/kelitis/no-existe/aprobar").principal(token("ze-001")))
                .andExpect(status().isNotFound());

        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void deberiaResponder404CuandoLaSunfyreDelKelitisNoExisteYNoGuardarNada() throws Exception {
        when(kelitisRepository.obtener("kl-001")).thenReturn(Optional.of(kelitisRetenido()));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/kelitis/kl-001/aprobar").principal(token("ze-001")))
                .andExpect(status().isNotFound());

        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void deberiaResponder422SiElServicioNoHaTerminadoYNoGuardarNada() throws Exception {
        when(kelitisRepository.obtener("kl-001")).thenReturn(Optional.of(kelitisRetenido()));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyreAbonada()));

        mockMvc.perform(put("/api/kelitis/kl-001/aprobar").principal(token("ze-001")))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("Solo se puede aprobar un servicio ya ejecutado"));

        verify(kelitisRepository, never()).guardar(any());
    }

    @Test
    void deberiaResponder422SiOtroZentysIntentaAprobarYNoGuardarNada() throws Exception {
        when(kelitisRepository.obtener("kl-001")).thenReturn(Optional.of(kelitisRetenido()));

        mockMvc.perform(put("/api/kelitis/kl-001/aprobar").principal(token("ze-999")))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensaje").value("Solo el Zentys del servicio puede aprobar este Kelitis"));

        verify(kelitisRepository, never()).guardar(any());
        verify(sunfyreRepository, never()).obtener(any());
    }
}
