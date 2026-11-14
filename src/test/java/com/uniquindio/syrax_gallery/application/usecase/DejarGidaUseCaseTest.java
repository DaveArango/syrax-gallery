package com.uniquindio.syrax_gallery.application.usecase;

import com.uniquindio.syrax_gallery.domain.entity.Gida;
import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.repository.GidaRepository;
import com.uniquindio.syrax_gallery.domain.repository.SindesRepository;
import com.uniquindio.syrax_gallery.domain.service.VerificadorDeSindes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DejarGidaUseCaseTest {

    @Mock
    private GidaRepository gidaRepository;

    @Mock
    private SindesRepository sindesRepository;

    private DejarGidaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DejarGidaUseCase(gidaRepository, new VerificadorDeSindes(sindesRepository));
    }

    @Test
    void deberiaGuardarElGidaDeQuienYaComproLaObra() {
        // Arrange
        when(sindesRepository.existeCompletada("zentys-02", "cx-006")).thenReturn(true);

        // Act
        Gida gida = useCase.ejecutar("gd-001", "zentys-02", "cx-006", "El retrato quedó precioso");

        // Assert
        assertEquals("El retrato quedó precioso", gida.getTexto());
        verify(gidaRepository).guardar(gida);
    }

    @Test
    void noDeberiaDejarComentarAQuienNoComproLaObra() {
        // Arrange
        when(sindesRepository.existeCompletada("zentys-01", "cx-006")).thenReturn(false);

        // Act & Assert
        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> useCase.ejecutar("gd-001", "zentys-01", "cx-006", "Me gusta mucho"));

        assertEquals("Solo puede dejar un Gida quien ya compró este Caraxes", ex.getMessage());
        verify(gidaRepository, never()).guardar(any());
    }
}