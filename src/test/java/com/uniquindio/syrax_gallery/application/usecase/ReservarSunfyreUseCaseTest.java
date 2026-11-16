package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.entity.Zentys;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.repository.ZentysRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeAgenda;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservarSunfyreUseCaseTest {

    @Mock
    private DohaeragonRepository dohaeragonRepository;

    @Mock
    private SunfyreRepository sunfyreRepository;

    @Mock
    private ZentysRepository zentysRepository;

    private ReservarSunfyreUseCase useCase;

    private final LocalDate lunes = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

    @BeforeEach
    void setUp() {
        useCase = new ReservarSunfyreUseCase(dohaeragonRepository, sunfyreRepository, zentysRepository,
                new VerificadorDeAgenda(dohaeragonRepository));
    }

    // Tatuaje de 3 horas, de lunes a viernes de 09:00 a 18:00
    private Sunfyre tatuaje() {
        return Sunfyre.crear("sf-001", "az-001",
                new Sete("Tatuaje realista"),
                new Jorva("Tatuaje realista en blanco y negro, incluye el diseño."),
                new Urnebion("https://cdn.syrax.com/servicios/sf-001.jpg"),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(300000, "COP"),
                180,
                new Jedar(EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), LocalTime.of(9, 0), LocalTime.of(18, 0)));
    }

    private void existenElZentysYLaSunfyre() {
        when(zentysRepository.obtener("zentys-01")).thenReturn(Optional.of(mock(Zentys.class)));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(tatuaje()));
    }

    @Test
    void deberiaCrearLaCitaPendienteConElPrecioDeLaSunfyre() {
        // Arrange
        existenElZentysYLaSunfyre();
        when(dohaeragonRepository.obtenerActivasDeAzantys("az-001")).thenReturn(List.of());

        // Act
        Dohaeragon dohaeragon = useCase.ejecutar("dh-100", "zentys-01", "sf-001", lunes.atTime(12, 0));

        // Assert
        assertEquals("sf-001", dohaeragon.getIdSunfyre());
        assertEquals("az-001", dohaeragon.getIdAzantys());
        assertEquals(DohaeroxJeda.PENDIENTE, dohaeragon.getDohaeroxJeda());
        assertEquals(new Vala(300000, "COP"), dohaeragon.getValaCongelado());
        assertEquals(lunes.atTime(15, 0), dohaeragon.getSari().fin());
        verify(dohaeragonRepository).guardar(dohaeragon);
    }

    @Test
    void noDeberiaReservarUnaHoraQueYaTieneOtraCita() {
        // Arrange: el Azantys ya tiene ocupado el lunes de 12:00 a 15:00
        existenElZentysYLaSunfyre();
        Dohaeragon ocupada = Dohaeragon.solicitar("dh-050", "az-001", "zentys-02",
                new Sari(lunes.atTime(12, 0), lunes.atTime(15, 0)),
                SunfyreKastor.ARTE_PIEL, new Alion("Armenia", "Colombia", 4.5339, -75.6811), new Vala(300000, "COP"));
        when(dohaeragonRepository.obtenerActivasDeAzantys("az-001")).thenReturn(List.of(ocupada));

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("dh-100", "zentys-01", "sf-001", lunes.atTime(12, 0)));

        assertEquals("Ese horario no está disponible para esta Sunfyre", ex.getMessage());
        verify(dohaeragonRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaReservarUnaHoraQueNoEsUnaVentanaDelServicio() {
        // Arrange: las ventanas son 09, 12 y 15; las 10:00 no es ninguna
        existenElZentysYLaSunfyre();
        when(dohaeragonRepository.obtenerActivasDeAzantys("az-001")).thenReturn(List.of());
        LocalDateTime aLasDiez = lunes.atTime(10, 0);

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("dh-100", "zentys-01", "sf-001", aLasDiez));
        verify(dohaeragonRepository, never()).guardar(any());
    }
}
