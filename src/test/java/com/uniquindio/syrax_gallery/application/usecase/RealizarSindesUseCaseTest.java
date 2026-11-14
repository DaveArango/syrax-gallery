package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Caraxes;
import com.uniquindio.syrax_gallery.domain.entity.Sindes;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.CaraxesRepository;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeSindes;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealizarSindesUseCaseTest {

    @Mock
    private SindesRepository sindesRepository;

    @Mock
    private CaraxesRepository caraxesRepository;

    @Mock
    private ZentysRepository zentysRepository;

    private RealizarSindesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RealizarSindesUseCase(sindesRepository, caraxesRepository, zentysRepository,
                new VerificadorDeSindes(sindesRepository));
    }

    private Zentys zentysActivo() {
        return Zentys.reconstruir("zentys-01", "Alicent Hightower",
                new Runiapos("alicent.hightower@gmail.com"),
                new Indior("Me interesan los grabados y la pintura al oleo."), true);
    }

    private Caraxes caraxes(Kanez kanez) {
        return Caraxes.reconstruir("cx-004", "az-001", new Vala(450000, "COP"),
                new Sete("Calles de Salento"),
                new Jorva("Grabado en linoleo con tinta negra sobre papel artesanal."),
                new Urnebion("https://cdn.syrax.com/obras/cx-004.jpg"),
                CaraxesKastor.GRABADO, kanez);
    }

    @Test
    void deberiaCrearUnSindesPendienteConElValaDeLaObra() {
        // Arrange
        when(zentysRepository.obtener("zentys-01")).thenReturn(Optional.of(zentysActivo()));
        when(caraxesRepository.obtener("cx-004")).thenReturn(Optional.of(caraxes(Kanez.EN_VENTA)));
        when(sindesRepository.existePendientePorCaraxes("cx-004")).thenReturn(false);

        // Act
        Sindes sindes = useCase.ejecutar("sd-001", "zentys-01", "cx-004");

        // Assert
        assertEquals(SindesJeda.PENDIENTE, sindes.getSindesJeda());
        assertEquals("az-001", sindes.getIdAzantys());
        assertEquals(new Vala(450000, "COP"), sindes.getValaCongelado());
        verify(sindesRepository).guardar(sindes);
    }

    @Test
    void noDeberiaComprarUnCaraxesQueNoEstaEnVenta() {
        // Arrange
        when(zentysRepository.obtener("zentys-01")).thenReturn(Optional.of(zentysActivo()));
        when(caraxesRepository.obtener("cx-004")).thenReturn(Optional.of(caraxes(Kanez.EXHIBICION)));

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("sd-001", "zentys-01", "cx-004"));

        assertEquals("Solo se puede comprar un Caraxes EN_VENTA", ex.getMessage());
        verify(sindesRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaComprarUnCaraxesQueYaTieneUnSindesPendiente() {
        // Arrange
        when(zentysRepository.obtener("zentys-01")).thenReturn(Optional.of(zentysActivo()));
        when(caraxesRepository.obtener("cx-004")).thenReturn(Optional.of(caraxes(Kanez.EN_VENTA)));
        when(sindesRepository.existePendientePorCaraxes("cx-004")).thenReturn(true);

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("sd-001", "zentys-01", "cx-004"));

        assertEquals("Este Caraxes ya tiene un Sindes pendiente", ex.getMessage());
        verify(sindesRepository, never()).guardar(any());
    }
}