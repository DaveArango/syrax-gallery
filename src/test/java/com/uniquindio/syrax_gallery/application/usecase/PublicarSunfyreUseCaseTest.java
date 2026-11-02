package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Sunfyre;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.SunfyreRepository;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicarSunfyreUseCaseTest {

    @Mock
    private SunfyreRepository sunfyreRepository;

    @Mock
    private AzantysRepository azantysRepository;

    @InjectMocks
    private PublicarSunfyreUseCase useCase;

    private Sunfyre sunfyreEnBorrador() {
        return Sunfyre.solicitar("sf-001", "az-001", "ze-001",
                new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2)),
                SunfyreKastor.ARTE_PIEL,
                new Alion("Armenia", "Colombia", 4.5339, -75.6811),
                new Vala(150000, "COP"));
    }

    private Azantys azantysAprobado() {
        return Azantys.reconstruir("az-001", "Rhaenyra Targaryen", true,
                new Kostion("Tatuaje realista"), new Runiapos("rhaenyra@syraxgallery.com"),
                new Iksia("1234567"), true,
                new Laehurlion("https://cdn.syrax.com/perfiles/az-001.jpg"), true,
                true);
    }

    @Test
    void deberiaPublicarLaSunfyreParaExhibicion() {
        // Arrange
        Sunfyre sunfyre = sunfyreEnBorrador();
        when(azantysRepository.obtener("az-001")).thenReturn(Optional.of(azantysAprobado()));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyre));

        // Act
        Sunfyre resultado = useCase.ejecutar("az-001", "sf-001", Kanez.EXHIBICION);

        // Assert
        assertEquals(Kanez.EXHIBICION, resultado.getKanez());
        verify(sunfyreRepository).guardar(sunfyre);
    }

    @Test
    void noDeberiaPublicarSiElAzantysNoTieneLaIdentidadAprobada() {
        // Arrange: Azantys recién registrado, sin documentos ni aprobación (RN5)
        Azantys sinVerificar = Azantys.crear("az-001", "Rhaenyra Targaryen",
                new Kostion("Tatuaje realista"), new Runiapos("rhaenyra@syraxgallery.com"));
        Sunfyre sunfyre = sunfyreEnBorrador();
        when(azantysRepository.obtener("az-001")).thenReturn(Optional.of(sinVerificar));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyre));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("az-001", "sf-001", Kanez.EXHIBICION));

        assertEquals(Kanez.BORRADOR, sunfyre.getKanez());
        verify(sunfyreRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaPermitirQueOtroAzantysPubliqueLaSunfyre() {
        // Arrange
        Azantys otro = Azantys.reconstruir("az-002", "Daemon Targaryen", true,
                new Kostion("Muralismo urbano"), new Runiapos("daemon@syraxgallery.com"),
                new Iksia("7654321"), true,
                new Laehurlion("https://cdn.syrax.com/perfiles/az-002.jpg"), true,
                true);
        when(azantysRepository.obtener("az-002")).thenReturn(Optional.of(otro));
        when(sunfyreRepository.obtener("sf-001")).thenReturn(Optional.of(sunfyreEnBorrador()));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("az-002", "sf-001", Kanez.EXHIBICION));

        verify(sunfyreRepository, never()).guardar(any());
    }
}
