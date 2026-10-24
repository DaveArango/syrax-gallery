package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SunfyreTest {

    private Sari sariFuturo() {
        return new Sari(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2));
    }

    private Alion alionArmenia() {
        return new Alion("Armenia", "Colombia", 4.5339, -75.6811);
    }

    private Sunfyre sunfyrePendiente() {
        return Sunfyre.solicitar("sf-001", "az-001", "ze-001", sariFuturo(),
                SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP"));
    }

    @Test
    void solicitarCreaUnaSunfyrePendienteYEnBorrador() {
        Sunfyre sunfyre = sunfyrePendiente();

        assertEquals(DohaeroxJeda.PENDIENTE, sunfyre.getDohaeroxJeda());
        assertEquals(Kanez.BORRADOR, sunfyre.getKanez());
    }

    @Test
    void noPermitirSolicitarUnaSunfyreEnElPasado() {
        Sari sariPasado = new Sari(LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2));

        assertThrows(ReglaDominioException.class, () -> Sunfyre.solicitar("sf-001", "az-001", "ze-001",
                sariPasado, SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP")));
    }

    @Test
    void noPermitirSolicitarSinZentys() {
        assertThrows(ReglaDominioException.class, () -> Sunfyre.solicitar("sf-001", "az-001", " ",
                sariFuturo(), SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP")));
    }

    @Test
    void publicarParaExhibicionDesdeBorrador() {
        Sunfyre sunfyre = sunfyrePendiente();

        sunfyre.publicar(Kanez.EXHIBICION);

        assertEquals(Kanez.EXHIBICION, sunfyre.getKanez());
    }

    @Test
    void publicarParaVentaDesdeExhibicion() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.publicar(Kanez.EXHIBICION);

        sunfyre.publicar(Kanez.EN_VENTA);

        assertEquals(Kanez.EN_VENTA, sunfyre.getKanez());
    }

    @Test
    void noPermitirPonerEnVentaUnaSunfyreConPrecioCero() {
        Sunfyre sunfyre = Sunfyre.solicitar("sf-002", "az-001", "ze-001", sariFuturo(),
                SunfyreKastor.MURAL, alionArmenia(), new Vala(0, "COP"));

        assertThrows(ReglaDominioException.class, () -> sunfyre.publicar(Kanez.EN_VENTA));

        assertEquals(Kanez.BORRADOR, sunfyre.getKanez());
    }

    @Test
    void noPermitirPublicarComoVendido() {
        Sunfyre sunfyre = sunfyrePendiente();

        assertThrows(ReglaDominioException.class, () -> sunfyre.publicar(Kanez.VENDIDO));
    }

    @Test
    void noPermitirPublicarUnaSunfyreAbonada() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.confirmar();

        assertThrows(ReglaDominioException.class, () -> sunfyre.publicar(Kanez.EXHIBICION));

        assertEquals(Kanez.BORRADOR, sunfyre.getKanez());
    }

    @Test
    void validarAutoriaAceptaAlAzantysCreador() {
        Sunfyre sunfyre = sunfyrePendiente();

        assertDoesNotThrow(() -> sunfyre.validarAutoria("az-001"));
    }

    @Test
    void validarAutoriaRechazaAOtroAzantys() {
        Sunfyre sunfyre = sunfyrePendiente();

        assertThrows(ReglaDominioException.class, () -> sunfyre.validarAutoria("az-999"));
    }

    @Test
    void noPermitirReprogramarUnaSunfyrePendiente() {
        Sunfyre sunfyre = sunfyrePendiente();
        Sari nuevoSari = new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2));

        assertThrows(ReglaDominioException.class, () -> sunfyre.reprogramar(nuevoSari));

        assertEquals(DohaeroxJeda.PENDIENTE, sunfyre.getDohaeroxJeda());
    }

    @Test
    void noPermitirReprogramarHaciaElPasado() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.confirmar();
        Sari sariPasado = new Sari(LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2));

        assertThrows(ReglaDominioException.class, () -> sunfyre.reprogramar(sariPasado));
    }

    @Test
    void noPermitirCancelarUnaSunfyreYaCompletada() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.confirmar();
        sunfyre.iniciarEjecucion();
        sunfyre.finalizar();

        assertThrows(ReglaDominioException.class, () -> sunfyre.cancelar("Ya no la quiero"));

        assertEquals(DohaeroxJeda.COMPLETADO, sunfyre.getDohaeroxJeda()); // sigue completada, no se canceló
    }

    @Test
    void reconstruirConservaElEstadoAunqueLaCitaYaHayaPasado() {
        Sari sariPasado = new Sari(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10).plusHours(3));

        Sunfyre sunfyre = Sunfyre.reconstruir("sf-009", "az-001", "ze-001", sariPasado,
                SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP"),
                DohaeroxJeda.COMPLETADO, Kanez.EN_VENTA);

        assertEquals(DohaeroxJeda.COMPLETADO, sunfyre.getDohaeroxJeda());
        assertEquals(Kanez.EN_VENTA, sunfyre.getKanez());
    }

    @Test
    void iniciarEjecucionDeUnaSunfyreAbonada() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.confirmar();

        sunfyre.iniciarEjecucion();

        assertEquals(DohaeroxJeda.EN_EJECUCION, sunfyre.getDohaeroxJeda());
    }

    @Test
    void noPermitirIniciarEjecucionDeUnaSunfyrePendiente() {
        Sunfyre sunfyre = sunfyrePendiente();

        assertThrows(ReglaDominioException.class, sunfyre::iniciarEjecucion);

        assertEquals(DohaeroxJeda.PENDIENTE, sunfyre.getDohaeroxJeda());
    }

    @Test
    void finalizarUnaSunfyreEnEjecucion() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.confirmar();
        sunfyre.iniciarEjecucion();

        sunfyre.finalizar();

        assertEquals(DohaeroxJeda.COMPLETADO, sunfyre.getDohaeroxJeda());
    }

    @Test
    void noPermitirFinalizarSinHaberIniciadoLaEjecucion() {
        Sunfyre sunfyre = sunfyrePendiente();
        sunfyre.confirmar();

        assertThrows(ReglaDominioException.class, sunfyre::finalizar);

        assertEquals(DohaeroxJeda.ABONADO, sunfyre.getDohaeroxJeda());
    }
}