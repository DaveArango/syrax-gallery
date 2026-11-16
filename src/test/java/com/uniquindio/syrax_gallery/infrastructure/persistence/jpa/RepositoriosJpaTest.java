package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.AzantysServicios;
import com.uniquindio.syrax_gallery.domain.repository.KelitisRepository;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class RepositoriosJpaTest {

    @Autowired
    private DohaeragonRepository dohaeragonRepository;

    @Autowired
    private KelitisRepository kelitisRepository;

    @Autowired
    private AzantysRepository azantysRepository;

    @Test
    void deberiaGuardarYRecuperarUnaDohaeragonConSusValueObjects() {
        LocalDateTime inicio = LocalDateTime.of(2028, 1, 15, 9, 0);
        LocalDateTime fin = LocalDateTime.of(2028, 1, 15, 12, 0);
        Dohaeragon dohaeragon = Dohaeragon.solicitar("dh-test-01", "az-001", "zentys-01",
                new Sari(inicio, fin),
                SunfyreKastor.MURAL,
                new Alion("Salento", "Colombia", 4.6377, -75.5703),
                new Vala(480000, "COP"));

        dohaeragonRepository.guardar(dohaeragon);
        Dohaeragon recuperada = dohaeragonRepository.obtener("dh-test-01").orElseThrow();

        assertEquals(new Sari(inicio, fin), recuperada.getSari());
        assertEquals(new Vala(480000, "COP"), recuperada.getValaCongelado());
        assertEquals("Salento", recuperada.getAlion().ciudad());
        assertEquals(SunfyreKastor.MURAL, recuperada.getKastor());
        assertEquals(DohaeroxJeda.PENDIENTE, recuperada.getDohaeroxJeda());
        assertEquals(Kanez.BORRADOR, recuperada.getKanez());
    }

    @Test
    void deberiaGuardarYRecuperarUnKelitisConSuMontoYEstado() {
        Kelitis kelitis = Kelitis.realizar("kl-test-01", "dh-007", "az-004", "zentys-01",
                new Vala(220000, "COP"));
        kelitis.liberarPago();

        kelitisRepository.guardar(kelitis);
        Kelitis recuperado = kelitisRepository.obtener("kl-test-01").orElseThrow();

        assertEquals("dh-007", recuperado.getIdDohaeragon());
        assertEquals(new Vala(220000, "COP"), recuperado.getValaCongelado());
        assertEquals(Gelior.LIBERADO, recuperado.getGelior());
    }

    @Test
    void elCatalogoDeberiaPaginarLasDohaeragonPublicadasDeDataSql() {

        Pagina<Dohaeragon> pagina = dohaeragonRepository.buscarPublicadas(null, null, null, null, 0, 2);

        assertEquals(9, pagina.totalElementos());
        assertEquals(5, pagina.totalPaginas());
        assertEquals(List.of("dh-007", "dh-008"),
                pagina.contenido().stream().map(Dohaeragon::getId).toList());
    }

    @Test
    void elRankingDeberiaOrdenarALosAzantysPorDohaeragonCompletadas() {

        List<AzantysServicios> ranking = azantysRepository.masSolicitados(3);

        assertEquals(2, ranking.size());
        assertEquals("az-001", ranking.get(0).azantysId());
        assertEquals(2L, ranking.get(0).servicios());
        assertEquals("az-004", ranking.get(1).azantysId());
        assertEquals(1L, ranking.get(1).servicios());
    }
}
