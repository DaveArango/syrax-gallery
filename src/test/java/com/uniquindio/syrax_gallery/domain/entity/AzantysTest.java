package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AzantysTest {
    private Kostion kostionValida() {
        return new Kostion("Tatuaje realista y retratos en blanco y negro");
    }

    private Runiapos runiaposValida() {
        return new Runiapos("rhaenyra@syraxgallery.com");
    }

    @Test
    void crearAzantysValidoConDatosCorrectos() {
        String id = "az-001";
        String nombre = "Rhaenyra Targaryen";
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        Azantys azantys = Azantys.crear(id, nombre, kostion, runiapos, null);

        assertEquals(id, azantys.getId());
        assertEquals(nombre, azantys.getNombre());
        assertTrue(azantys.isActivo());
        assertFalse(azantys.isIksiaVerificada());
        assertNull(azantys.getLaehurlion());
    }

    @Test
    void idEsNulo() {
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear(null, "Rhaenyra", kostion, runiapos, null));
    }

    @Test
    void nombreEsNuloOVacio() {
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", null, kostion, runiapos, null));
        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", "  ", kostion, runiapos, null));
    }

    @Test
    void lentorIdEsCadenaVacia() {
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", "Rhaenyra", kostion, runiapos, ""));

        assertEquals("El lentorId no puede ser una cadena vacía", ex.getMessage());
    }

    @Test
    void lentorIdNuloParaArtistaIndependiente() {
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        Azantys azantys = Azantys.crear("az-001", "Rhaenyra", kostion, runiapos, null);

        assertNull(azantys.getLentorId());
    }

    @Test
    void verificarIdentidadDosVeces() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);
        azantys.verificarIdentidad(new Iksia("1234567"));

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> azantys.verificarIdentidad(new Iksia("7654321")));

        assertEquals("La identidad ya fue verificada", ex.getMessage());
    }

    @Test
    void aunNoEstaVerificadaIdentidad() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);

        azantys.completarIdentificacion();

        assertTrue(azantys.isIksiaVerificada());
    }

    @Test
    void noPoderPublicarSinIdentidadNiFoto() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);

        boolean resultado = azantys.puedePublicar();

        assertFalse(resultado);
    }

    @Test
    void noPublicarSoloConIdentidadVerificada() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);
        azantys.verificarIdentidad(new Iksia("1234567"));

        boolean resultado = azantys.puedePublicar();

        assertFalse(resultado);
    }

    @Test
    void noPublicarSoloConFotoDePerfil() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);
        azantys.subirLaehurlion(new Laehurlion("https://cdn.syraxgallery.com/az-001.png"));

        boolean resultado = azantys.puedePublicar();

        assertFalse(resultado);
    }

    @Test
    void publicarConIdentidadVerificadaYFotoDePerfil() {
        Azantys azantys = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);
        azantys.verificarIdentidad(new Iksia("1234567"));
        azantys.subirLaehurlion(new Laehurlion("https://cdn.syraxgallery.com/az-001.png"));

        boolean resultado = azantys.puedePublicar();

        assertTrue(resultado);
    }
}

