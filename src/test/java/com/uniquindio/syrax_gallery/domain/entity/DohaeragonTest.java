package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DohaeragonTest {

    private Sari sariFuturo() {
        return new Sari(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2));
    }

    private Alion alionArmenia() {
        return new Alion("Armenia", "Colombia", 4.5339, -75.6811);
    }

    private Dohaeragon dohaeragonPendiente() {
        return Dohaeragon.solicitar("dh-001", "az-001", "ze-001", sariFuturo(),
                SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP"));
    }

    @Test
    void solicitarCreaUnaDohaeragonPendienteYEnBorrador() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertEquals(DohaeroxJeda.PENDIENTE, dohaeragon.getDohaeroxJeda());
        assertEquals(Kanez.BORRADOR, dohaeragon.getKanez());
    }

    @Test
    void noPermitirSolicitarUnaDohaeragonEnElPasado() {
        Sari sariPasado = new Sari(LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2));

        assertThrows(ReglaDominioException.class, () -> Dohaeragon.solicitar("dh-001", "az-001", "ze-001",
                sariPasado, SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP")));
    }

    @Test
    void noPermitirSolicitarSinZentys() {
        assertThrows(ReglaDominioException.class, () -> Dohaeragon.solicitar("dh-001", "az-001", " ",
                sariFuturo(), SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP")));
    }

    @Test
    void publicarParaExhibicionDesdeBorrador() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        dohaeragon.publicar(Kanez.EXHIBICION);

        assertEquals(Kanez.EXHIBICION, dohaeragon.getKanez());
    }

    @Test
    void publicarParaVentaDesdeExhibicion() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.publicar(Kanez.EXHIBICION);

        dohaeragon.publicar(Kanez.EN_VENTA);

        assertEquals(Kanez.EN_VENTA, dohaeragon.getKanez());
    }

    @Test
    void noPermitirPonerEnVentaUnaDohaeragonConPrecioCero() {
        Dohaeragon dohaeragon = Dohaeragon.solicitar("dh-002", "az-001", "ze-001", sariFuturo(),
                SunfyreKastor.MURAL, alionArmenia(), new Vala(0, "COP"));

        assertThrows(ReglaDominioException.class, () -> dohaeragon.publicar(Kanez.EN_VENTA));

        assertEquals(Kanez.BORRADOR, dohaeragon.getKanez());
    }

    @Test
    void noPermitirPublicarComoVendido() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.publicar(Kanez.VENDIDO));
    }

    @Test
    void noPermitirPublicarUnaDohaeragonAbonada() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.publicar(Kanez.EXHIBICION));

        assertEquals(Kanez.BORRADOR, dohaeragon.getKanez());
    }

    @Test
    void validarAutoriaAceptaAlAzantysCreador() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertDoesNotThrow(() -> dohaeragon.validarAutoria("az-001"));
    }

    @Test
    void validarAutoriaRechazaAOtroAzantys() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.validarAutoria("az-999"));
    }

    @Test
    void noPermitirReprogramarUnaDohaeragonPendiente() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        Sari nuevoSari = new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2));

        assertThrows(ReglaDominioException.class, () -> dohaeragon.reprogramar(nuevoSari));

        assertEquals(DohaeroxJeda.PENDIENTE, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void noPermitirReprogramarHaciaElPasado() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();
        Sari sariPasado = new Sari(LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2));

        assertThrows(ReglaDominioException.class, () -> dohaeragon.reprogramar(sariPasado));
    }

    @Test
    void noPermitirCancelarUnaDohaeragonYaCompletada() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();
        dohaeragon.iniciarEjecucion();
        dohaeragon.finalizar();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.cancelar("Ya no la quiero"));

        assertEquals(DohaeroxJeda.COMPLETADO, dohaeragon.getDohaeroxJeda()); // sigue completada, no se canceló
    }

    @Test
    void reconstruirConservaElEstadoAunqueLaCitaYaHayaPasado() {
        Sari sariPasado = new Sari(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10).plusHours(3));

        Dohaeragon dohaeragon = Dohaeragon.reconstruir("dh-009", "az-001", "ze-001", sariPasado,
                SunfyreKastor.ARTE_PIEL, alionArmenia(), new Vala(150000, "COP"),
                DohaeroxJeda.COMPLETADO, Kanez.EN_VENTA);

        assertEquals(DohaeroxJeda.COMPLETADO, dohaeragon.getDohaeroxJeda());
        assertEquals(Kanez.EN_VENTA, dohaeragon.getKanez());
    }

    @Test
    void iniciarEjecucionDeUnaDohaeragonAbonada() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();

        dohaeragon.iniciarEjecucion();

        assertEquals(DohaeroxJeda.EN_EJECUCION, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void noPermitirIniciarEjecucionDeUnaDohaeragonPendiente() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertThrows(ReglaDominioException.class, dohaeragon::iniciarEjecucion);

        assertEquals(DohaeroxJeda.PENDIENTE, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void finalizarUnaDohaeragonEnEjecucion() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();
        dohaeragon.iniciarEjecucion();

        dohaeragon.finalizar();

        assertEquals(DohaeroxJeda.COMPLETADO, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void noPermitirFinalizarSinHaberIniciadoLaEjecucion() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();

        assertThrows(ReglaDominioException.class, dohaeragon::finalizar);

        assertEquals(DohaeroxJeda.ABONADO, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void cancelarUnaDohaeragonAbonada() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();

        dohaeragon.cancelar("El cliente viaja esa semana");

        assertEquals(DohaeroxJeda.CANCELADO, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void noPermitirCancelarUnaDohaeragonEnEjecucion() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();
        dohaeragon.iniciarEjecucion();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.cancelar("Ya no la quiero"));

        assertEquals(DohaeroxJeda.EN_EJECUCION, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void noPermitirCancelarSinMotivo() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.cancelar(""));

        assertEquals(DohaeroxJeda.PENDIENTE, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void validarParticipanteAceptaAlAzantysYAlZentys() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertDoesNotThrow(() -> dohaeragon.validarParticipante("az-001"));
        assertDoesNotThrow(() -> dohaeragon.validarParticipante("ze-001"));
    }

    @Test
    void validarParticipanteRechazaAUnTercero() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.validarParticipante("ze-999"));
    }

    @Test
    void reprogramarUnaDohaeragonAbonada() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();
        Sari nuevo = new Sari(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(2));

        dohaeragon.reprogramar(nuevo);

        assertEquals(nuevo, dohaeragon.getSari());
        assertEquals(DohaeroxJeda.ABONADO, dohaeragon.getDohaeroxJeda());
    }

    @Test
    void validarClienteAceptaAlZentysDeLaDohaeragon() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertDoesNotThrow(() -> dohaeragon.validarCliente("ze-001"));
    }

    @Test
    void validarClienteRechazaAlAzantysYAOtroZentys() {
        Dohaeragon dohaeragon = dohaeragonPendiente();

        assertThrows(ReglaDominioException.class, () -> dohaeragon.validarCliente("az-001"));
        assertThrows(ReglaDominioException.class, () -> dohaeragon.validarCliente("ze-999"));
    }

    @Test
    void validarCompletadaAceptaUnaDohaeragonCompletada() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();
        dohaeragon.iniciarEjecucion();
        dohaeragon.finalizar();

        assertDoesNotThrow(dohaeragon::validarCompletada);
    }

    @Test
    void validarCompletadaRechazaUnaDohaeragonQueAunNoTermina() {
        Dohaeragon dohaeragon = dohaeragonPendiente();
        dohaeragon.confirmar();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, dohaeragon::validarCompletada);

        assertEquals("Solo se puede aprobar un servicio ya ejecutado", ex.getMessage());
    }
}