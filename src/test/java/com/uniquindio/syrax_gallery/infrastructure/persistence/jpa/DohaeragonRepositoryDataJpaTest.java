package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.repository.Pagina;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.DohaeragonPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba del adaptador de persistencia contra una base H2 real (no simulada):
 * comprueba que el mapper guarda y reconstruye bien la Dohaeragon y que la consulta JPQL filtra en la base de datos.
 * Usa la ciudad "Filandia" para no mezclarse con los datos de data.sql.
 */
@DataJpaTest
@Import({DohaeragonRepositoryJpa.class, DohaeragonPersistenceMapper.class})
class DohaeragonRepositoryDataJpaTest {

    @Autowired
    private DohaeragonRepositoryJpa repository;

    private Dohaeragon dohaeragonEnFilandia(String id, double monto) {
        return Dohaeragon.solicitar(id, "az-900", "ze-900",
                new Sari(LocalDateTime.now().plusDays(30), LocalDateTime.now().plusDays(30).plusHours(2)),
                SunfyreKastor.MURAL,
                new Alion("Filandia", "Colombia", 4.6747, -75.6581),
                new Vala(monto, "COP"));
    }

    @Test
    void deberiaGuardarYRecuperarUnaDohaeragon() {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonEnFilandia("dh-test-1", 250000);

        // Act
        repository.guardar(dohaeragon);
        Optional<Dohaeragon> encontrada = repository.obtener("dh-test-1");

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
        Dohaeragon barata = dohaeragonEnFilandia("dh-test-2", 100000);
        barata.publicar(Kanez.EN_VENTA);
        Dohaeragon cara = dohaeragonEnFilandia("dh-test-3", 900000);
        cara.publicar(Kanez.EN_VENTA);
        Dohaeragon borrador = dohaeragonEnFilandia("dh-test-4", 120000); // no publicada: no debe salir
        repository.guardar(barata);
        repository.guardar(cara);
        repository.guardar(borrador);

        // Act
        Pagina<Dohaeragon> pagina = repository.buscarPublicadas("filandia", null, 50000.0, 200000.0, 0, 10);

        // Assert
        assertEquals(1, pagina.totalElementos());
        assertEquals(1, pagina.totalPaginas());
        assertEquals("dh-test-2", pagina.contenido().get(0).getId());
    }
}
