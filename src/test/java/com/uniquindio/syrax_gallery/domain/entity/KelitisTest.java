package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.Gelior;
import com.uniquindio.syrax_gallery.domain.valueobject.Vala;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KelitisTest {

    private Kelitis kelitisRetenido() {
        Vala monto = new Vala(150000, "COP");
        return Kelitis.realizar("kl-001", "az-001", "ze-001", monto);
    }

    @Test
    void crearKelitisEnEstadoRetenido() {
        Vala monto = new Vala(150000, "COP");

        Kelitis kelitis = Kelitis.realizar("kl-001", "az-001", "ze-001", monto);

        assertEquals(Gelior.RETENIDO, kelitis.getGelior());
        assertEquals("az-001", kelitis.getIdAzantys());
        assertEquals("ze-001", kelitis.getIdZentys());
        assertEquals(monto, kelitis.getValaCongelado());
    }

    @Test
    void noDebeCrearKelitisSinMonto() {
        assertThrows(ReglaDominioException.class,
                () -> Kelitis.realizar(
                        "kl-001",
                        "az-001",
                        "ze-001",
                        null)
        );
    }

    @Test
    void liberarPagoCuandoEstaRetenido() {
        Kelitis kelitis = kelitisRetenido();

        kelitis.liberarPago();

        assertEquals(Gelior.LIBERADO, kelitis.getGelior());
    }

    @Test
    void noPermitirLiberarPagoUnKelitisYaLiberado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();

        assertThrows(ReglaDominioException.class, kelitis::liberarPago);

        assertEquals(Gelior.LIBERADO, kelitis.getGelior());
    }

    @Test
    void completarUnKelitisLiberado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();

        kelitis.completar();

        assertEquals(Gelior.COMPLETADO, kelitis.getGelior());
    }

    @Test
    void noPermitirCompletarUnKelitisRetenido() {
        Kelitis kelitis = kelitisRetenido();

        assertThrows(ReglaDominioException.class, kelitis::completar);

        assertEquals(Gelior.RETENIDO, kelitis.getGelior());
    }

    @Test
    void noPermitirReembolsarUnKelitisYaLiberado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();

        assertThrows(ReglaDominioException.class, () -> kelitis.solicitarReembolso("Servicio no prestado"));

        assertEquals(Gelior.LIBERADO, kelitis.getGelior());
    }

    @Test
    void debePermitirReembolsoCuandoEstaRetenido() {
        Kelitis kelitis = kelitisRetenido();

        kelitis.solicitarReembolso("Servicio cancelado");

        assertEquals(Gelior.REEMBOLSADO, kelitis.getGelior());
    }

    @Test
    void noPermitirReembolsoSinMotivo() {
        Kelitis kelitis = kelitisRetenido();

        assertThrows(ReglaDominioException.class, () -> kelitis.solicitarReembolso(""));

        assertEquals(Gelior.RETENIDO, kelitis.getGelior());
    }

    @Test
    void noPermitirReembolsoDeUnKelitisCompletado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();
        kelitis.completar();

        assertThrows(ReglaDominioException.class, () -> kelitis.solicitarReembolso("Quiero devolver el dinero"));

        assertEquals(Gelior.COMPLETADO, kelitis.getGelior());
    }
}
