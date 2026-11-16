package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Azantys;
import com.uniquindio.syrax_gallery.domain.entity.Dohaeragon;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.AzantysRepository;
import com.uniquindio.syrax_gallery.domain.repository.DohaeragonRepository;
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
class PublicarDohaeragonUseCaseTest {

    @Mock
    private DohaeragonRepository dohaeragonRepository;

    @Mock
    private AzantysRepository azantysRepository;

    @InjectMocks
    private PublicarDohaeragonUseCase useCase;

    private Dohaeragon dohaeragonEnBorrador() {
        return Dohaeragon.solicitar("dh-001", "az-001", "ze-001",
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
    void deberiaPublicarLaDohaeragonParaExhibicion() {
        // Arrange
        Dohaeragon dohaeragon = dohaeragonEnBorrador();
        when(azantysRepository.obtener("az-001")).thenReturn(Optional.of(azantysAprobado()));
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragon));

        // Act
        Dohaeragon resultado = useCase.ejecutar("az-001", "dh-001", Kanez.EXHIBICION);

        // Assert
        assertEquals(Kanez.EXHIBICION, resultado.getKanez());
        verify(dohaeragonRepository).guardar(dohaeragon);
    }

    @Test
    void noDeberiaPublicarSiElAzantysNoTieneLaIdentidadAprobada() {
        // Arrange: Azantys recién registrado, sin documentos ni aprobación (RN5)
        Azantys sinVerificar = Azantys.crear("az-001", "Rhaenyra Targaryen",
                new Kostion("Tatuaje realista"), new Runiapos("rhaenyra@syraxgallery.com"));
        Dohaeragon dohaeragon = dohaeragonEnBorrador();
        when(azantysRepository.obtener("az-001")).thenReturn(Optional.of(sinVerificar));
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragon));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("az-001", "dh-001", Kanez.EXHIBICION));

        assertEquals(Kanez.BORRADOR, dohaeragon.getKanez());
        verify(dohaeragonRepository, never()).guardar(any());
    }

    @Test
    void noDeberiaPermitirQueOtroAzantysPubliqueLaDohaeragon() {
        // Arrange
        Azantys otro = Azantys.reconstruir("az-002", "Daemon Targaryen", true,
                new Kostion("Muralismo urbano"), new Runiapos("daemon@syraxgallery.com"),
                new Iksia("7654321"), true,
                new Laehurlion("https://cdn.syrax.com/perfiles/az-002.jpg"), true,
                true);
        when(azantysRepository.obtener("az-002")).thenReturn(Optional.of(otro));
        when(dohaeragonRepository.obtener("dh-001")).thenReturn(Optional.of(dohaeragonEnBorrador()));

        // Act & Assert
        assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("az-002", "dh-001", Kanez.EXHIBICION));

        verify(dohaeragonRepository, never()).guardar(any());
    }
}
