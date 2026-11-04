package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa;

import com.uniquindio.syrax_gallery.domain.entity.Kelitis;
import com.uniquindio.syrax_gallery.domain.valueobject.Gelior;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.mapper.KelitisPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({KelitisRepositoryJpa.class, KelitisPersistenceMapper.class})
class KelitisRepositoryJpaTest {

    @Autowired
    private KelitisRepositoryJpa repository;

    @Test
    void deberiaEncontrarElKelitisDeUnaSunfyre() {
        Kelitis kelitis = Kelitis.realizar("kl-test-02", "sf-test-99", "az-900", "ze-900",
                new Vala(95000, "COP"));
        repository.guardar(kelitis);

        Optional<Kelitis> encontrado = repository.obtenerPorSunfyre("sf-test-99");

        assertTrue(encontrado.isPresent());
        assertEquals("kl-test-02", encontrado.get().getId());
        assertEquals(Gelior.RETENIDO, encontrado.get().getGelior());
    }

    @Test
    void existePorSunfyreDistingueUnaSunfyrePagadaDeUnaSinPagar() {
        repository.guardar(Kelitis.realizar("kl-test-03", "sf-test-pagada", "az-900", "ze-900",
                new Vala(95000, "COP")));

        boolean pagada = repository.existePorSunfyre("sf-test-pagada");
        boolean sinPagar = repository.existePorSunfyre("sf-test-sin-pagar");

        assertTrue(pagada);
        assertFalse(sinPagar);
    }
}
