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
        return Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida());
    }

    private Azantys azantysPendienteDeRevision() {
        Azantys azantys = azantysBase();
        azantys.verificarIksia(new Iksia("1234567"));
        azantys.subirLaehurlion(new Laehurlion("https://cdn.syraxgallery.com/az-001.png"));
        return azantys;
    }

    @Test
    void crearAzantysValidoConDatosCorrectos() {
        String id = "az-001";
        String nombre = "Rhaenyra Targaryen";
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        Azantys azantys = Azantys.crear(id, nombre, kostion, runiapos);

        assertEquals(id, azantys.getId());
        assertEquals(nombre, azantys.getNombre());
        assertEquals(kostion, azantys.getKostion());
        assertEquals(runiapos, azantys.getRuniapos());
        assertTrue(azantys.isActivo());
        assertFalse(azantys.isIksiaVerificada());
        assertFalse(azantys.isLaehurlionVerificado());
        assertFalse(azantys.isIdentidadAprobada());
        assertNull(azantys.getLaehurlion());
    }

    @Test
    void idEsNuloOVacio() {
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear(null, "Rhaenyra", kostion, runiapos));
        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("  ", "Rhaenyra", kostion, runiapos));
    }

    @Test
    void nombreEsNuloOVacio() {
        Kostion kostion = kostionValida();
        Runiapos runiapos = runiaposValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", null, kostion, runiapos));
        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", "  ", kostion, runiapos));
    }

    @Test
    void kostionEsNulo() {
        Runiapos runiapos = runiaposValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", "Rhaenyra", null, runiapos));
    }

    @Test
    void runiaposEsNulo() {
        Kostion kostion = kostionValida();

        assertThrows(ReglaDominioException.class,
                () -> Azantys.crear("az-001", "Rhaenyra", kostion, null));
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
    void dosAzantysConElMismoIdSonIguales() {
        Azantys a = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida());
        Azantys b = Azantys.crear("az-001", "Otro nombre", kostionValida(), runiaposValida());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void dosAzantysConDistintoIdNoSonIguales() {
        Azantys a = Azantys.crear("az-001", "Rhaenyra", kostionValida(), runiaposValida());
        Azantys b = Azantys.crear("az-002", "Rhaenyra", kostionValida(), runiaposValida());

        assertNotEquals(a, b);
    }

    @Test
    void alCrearQuedaPendienteDeVerificacion() {
        Azantys azantys = azantysBase();

        assertFalse(azantys.estaHabilitadoParaPublicar());
    }

    @Test
    void conIdentidadYFotoQuedaPendienteDeRevisionYAunNoPuedePublicar() {
        Azantys azantys = azantysPendienteDeRevision();

        assertTrue(azantys.estaPendienteDeRevision());
        assertFalse(azantys.estaHabilitadoParaPublicar());
        assertThrows(ReglaDominioException.class, azantys::validarHabilitacionParaPublicar);
    }

    @Test
    void aprobarIdentidadHabilitaParaPublicar() {
        Azantys azantys = azantysPendienteDeRevision();

        azantys.aprobarIdentidad();

        assertTrue(azantys.isIdentidadAprobada());
        assertFalse(azantys.estaPendienteDeRevision());
        assertTrue(azantys.validarHabilitacionParaPublicar());
    }

    @Test
    void noAprobarSinDocumentosCargados() {
        Azantys azantys = azantysBase();

        assertThrows(ReglaDominioException.class, azantys::aprobarIdentidad);

        assertFalse(azantys.isIdentidadAprobada());
    }

    @Test
    void noAprobarDosVeces() {
        Azantys azantys = azantysPendienteDeRevision();
        azantys.aprobarIdentidad();

        assertThrows(ReglaDominioException.class, azantys::aprobarIdentidad);
    }

    @Test
    void rechazarIdentidadBorraLaIksiaParaQueSeCargueDeNuevo() {
        Azantys azantys = azantysPendienteDeRevision();

        azantys.rechazarIdentidad("El documento no es legible");

        assertFalse(azantys.isIksiaVerificada());
        assertNull(azantys.getIksia());
        assertFalse(azantys.isIdentidadAprobada());
        assertDoesNotThrow(() -> azantys.verificarIksia(new Iksia("7654321")));
    }

    @Test
    void noRechazarSinMotivo() {
        Azantys azantys = azantysPendienteDeRevision();

        assertThrows(ReglaDominioException.class, () -> azantys.rechazarIdentidad(" "));

        assertTrue(azantys.isIksiaVerificada());
    }

    @Test
    void noRechazarUnaIdentidadYaAprobada() {
        Azantys azantys = azantysPendienteDeRevision();
        azantys.aprobarIdentidad();

        assertThrows(ReglaDominioException.class, () -> azantys.rechazarIdentidad("Cambio de opinión"));

        assertTrue(azantys.isIdentidadAprobada());
    }

    @Test
    void reconstruirConservaElEstadoDeVerificacion() {
        Iksia iksia = new Iksia("1234567");
        Laehurlion foto = new Laehurlion("https://cdn.syraxgallery.com/az-001.png");

        Azantys azantys = Azantys.reconstruir("az-001", "Rhaenyra", true,
                kostionValida(), runiaposValida(), iksia, true, foto, true, true);

        assertTrue(azantys.isIksiaVerificada());
        assertTrue(azantys.isLaehurlionVerificado());
        assertTrue(azantys.isIdentidadAprobada());
        assertEquals(iksia, azantys.getIksia());
        assertTrue(azantys.validarHabilitacionParaPublicar());
    }

    @Test
    void unAzantysReconstruidoYaVerificadoNoPuedeVerificarseOtraVez() {
        Azantys azantys = Azantys.reconstruir("az-001", "Rhaenyra", true,
                kostionValida(), runiaposValida(), new Iksia("1234567"), true, null, false, false);

        assertThrows(ReglaDominioException.class, () -> azantys.verificarIksia(new Iksia("7654321")));
    }
    
    @Test
    void validarTitularAceptaAlPropioAzantys() {
        Azantys azantys = azantysBase();

        assertDoesNotThrow(() -> azantys.validarTitular("az-001"));
    }

    @Test
    void validarTitularRechazaAOtroAzantys() {
        Azantys azantys = azantysBase();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> azantys.validarTitular("az-002"));

        assertEquals("Solo el propio Azantys puede cargar su identidad", ex.getMessage());
    }
}