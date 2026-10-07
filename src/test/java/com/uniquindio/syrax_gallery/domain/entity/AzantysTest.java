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

    private Azantys azantysBase() {
        return Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);
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
        assertEquals(kostion, azantys.getKostion());
        assertEquals(runiapos, azantys.getRuniapos());
        assertTrue(azantys.isActivo());
        assertFalse(azantys.isIksiaVerificada());
        assertFalse(azantys.isLaehurlionVerificado());
        assertNull(azantys.getLaehurlion());
    }

    @Test
    void idEsNuloOVacio() {
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear(null, "Rhaenyra", kostion, runiapos, null));
        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("  ", "Rhaenyra", kostion, runiapos, null));
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
    void kostionEsNulo() {
        Runiapos runiapos = runiaposValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", "Rhaenyra", null, runiapos, null));
    }

    @Test
    void runiaposEsNulo() {
        Kostion kostion = kostionValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", "Rhaenyra", kostion, null, null));
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
    void aunNoEstaVerificadaIdentidadAlCrear() {
        Azantys azantys = azantysBase();

        boolean verificada = azantys.isIksiaVerificada();

        assertFalse(verificada);
    }

    @Test
    void verificarIksiaMarcaLaIdentidadComoVerificada() {
        Azantys azantys = azantysBase();
        Iksia iksia = new Iksia("1234567");

        azantys.verificarIksia(iksia);

        assertTrue(azantys.isIksiaVerificada());
        assertEquals(iksia, azantys.getIksia());
    }

    @Test
    void verificarIdentidadDosVeces() {
        Azantys azantys = azantysBase();
        azantys.verificarIksia(new Iksia("1234567"));

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> azantys.verificarIksia(new Iksia("7654321")));

        assertEquals("La identidad ya fue verificada", ex.getMessage());
    }

    @Test
    void verificarIksiaConDocumentoNulo() {
        Azantys azantys = azantysBase();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> azantys.verificarIksia(null));

        assertEquals("El documento de identidad es obligatorio", ex.getMessage());
        assertFalse(azantys.isIksiaVerificada());
    }

    @Test
    void subirLaehurlionMarcaLaFotoComoVerificada() {
        Azantys azantys = azantysBase();
        Laehurlion foto = new Laehurlion("https://cdn.syraxgallery.com/az-001.png");

        azantys.subirLaehurlion(foto);

        assertTrue(azantys.isLaehurlionVerificado());
        assertEquals(foto, azantys.getLaehurlion());
    }

    @Test
    void subirLaehurlionNulaEsRechazada() {
        Azantys azantys = azantysBase();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> azantys.subirLaehurlion(null));

        assertEquals("La foto de perfil es obligatoria", ex.getMessage());
        assertFalse(azantys.isLaehurlionVerificado());
    }

    @Test
    void noPoderPublicarSinIdentidadNiFoto() {
        Azantys azantys = azantysBase();

        assertThrows(ReglaDominioException.class,
                azantys::validarHabilitacionParaPublicar);
    }

    @Test
    void noPublicarSoloConIdentidadVerificada() {
        Azantys azantys = azantysBase();
        azantys.verificarIksia(new Iksia("1234567"));

        assertThrows(ReglaDominioException.class,
                azantys::validarHabilitacionParaPublicar);
    }

    @Test
    void noPublicarSoloConFotoDePerfil() {
        Azantys azantys = azantysBase();
        azantys.subirLaehurlion(new Laehurlion("https://cdn.syraxgallery.com/az-001.png"));

        assertThrows(ReglaDominioException.class,
                azantys::validarHabilitacionParaPublicar);
    }

    @Test
    void publicarConIdentidadVerificadaYFotoDePerfil() {
        Azantys azantys = azantysBase();
        azantys.verificarIksia(new Iksia("1234567"));
        azantys.subirLaehurlion(new Laehurlion("https://cdn.syraxgallery.com/az-001.png"));

        boolean resultado = azantys.validarHabilitacionParaPublicar();

        assertTrue(resultado);
    }

    @Test
    void dosAzantysConElMismoIdSonIguales() {
        Azantys a = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);
        Azantys b = Azantys.crear("az-001", "Otro nombre", kostionValida(), runiaposValida(), "lentor-1");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void dosAzantysConDistintoIdNoSonIguales() {
        Azantys a = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida(), null);
        Azantys b = Azantys.crear("az-002", "Rhaenyra", kostionValida(), runiaposValida(), null);

        assertNotEquals(a, b);
    }
}