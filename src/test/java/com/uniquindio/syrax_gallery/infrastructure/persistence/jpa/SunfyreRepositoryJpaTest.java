package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.SunfyrePersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba del adaptador de persistencia contra una base H2 real (no simulada):
 * comprueba que el mapper guarda y reconstruye bien la Sunfyre y que la consulta JPQL filtra en la base de datos.
 * Usa la ciudad "Filandia" para no mezclarse con los datos de data.sql.
 */
@DataJpaTest
@Import({SunfyreRepositoryJpa.class, SunfyrePersistenceMapper.class})
class SunfyreRepositoryJpaTest {

    @Autowired
    private SunfyreRepositoryJpa repository;

    private Sunfyre sunfyreEnFilandia(String id, double monto) {
        return Sunfyre.solicitar(id, "az-900", "ze-900",
                new Sari(LocalDateTime.now().plusDays(30), LocalDateTime.now().plusDays(30).plusHours(2)),
                SunfyreKastor.MURAL,
                new Alion("Filandia", "Colombia", 4.6747, -75.6581),
                new Vala(monto, "COP"));
    }

    @Test
    void deberiaGuardarYRecuperarUnaSunfyre() {
        // Arrange
        Sunfyre sunfyre = sunfyreEnFilandia("sf-test-1", 250000);

        // Act
        repository.guardar(sunfyre);
        Optional<Sunfyre> encontrada = repository.obtener("sf-test-1");

        // Assert
        assertTrue(encontrada.isPresent());
        assertEquals("az-900", encontrada.get().getIdAzantys());
        assertEquals("Filandia", encontrada.get().getAlion().ciudad());
        assertEquals(250000, encontrada.get().getValaCongelado().monto());
        assertEquals(DohaeroxJeda.PENDIENTE, encontrada.get().getDohaeroxJeda());
        assertEquals(Kanez.BORRADOR, encontrada.get().getKanez());
    }

    @Test
    void elCatalogoSoloTraeLasPublicadasDentroDelRangoDePrecio() {
        // Arrange
        Sunfyre barata = sunfyreEnFilandia("sf-test-2", 100000);
        barata.publicar(Kanez.EN_VENTA);
        Sunfyre cara = sunfyreEnFilandia("sf-test-3", 900000);
        cara.publicar(Kanez.EN_VENTA);
        Sunfyre borrador = sunfyreEnFilandia("sf-test-4", 120000); // no publicada: no debe salir
        repository.guardar(barata);
        repository.guardar(cara);
        repository.guardar(borrador);

        // Act
        Pagina<Sunfyre> pagina = repository.buscarPublicadas("filandia", null, 50000.0, 200000.0, 0, 10);

        // Assert
        assertEquals(1, pagina.totalElementos());
        assertEquals(1, pagina.totalPaginas());
        assertEquals("sf-test-2", pagina.contenido().get(0).getId());
    }
}
