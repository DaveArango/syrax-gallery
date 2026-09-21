package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.EstadoKelitis;
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

        assertEquals(EstadoKelitis.RETENIDO, kelitis.getEstado());
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

        assertEquals(EstadoKelitis.LIBERADO, kelitis.getEstado());
    }

    @Test
    void noPermitirLiberarPagoUnKelitisYaLiberado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();

        assertThrows(ReglaDominioException.class, kelitis::liberarPago);

        assertEquals(EstadoKelitis.LIBERADO, kelitis.getEstado());
    }

    @Test
    void completarUnKelitisLiberado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();

        kelitis.completar();

        assertEquals(EstadoKelitis.COMPLETADO, kelitis.getEstado());
    }

    @Test
    void noPermitirCompletarUnKelitisRetenido() {
        Kelitis kelitis = kelitisRetenido();

        assertThrows(ReglaDominioException.class, kelitis::completar);

        assertEquals(EstadoKelitis.RETENIDO, kelitis.getEstado());
    }

    @Test
    void noPermitirReembolsarUnKelitisYaLiberado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();

        assertThrows(ReglaDominioException.class, () -> kelitis.solicitarReembolso("Servicio no prestado"));

        assertEquals(EstadoKelitis.LIBERADO, kelitis.getEstado());
    }

    @Test
    void debePermitirReembolsoCuandoEstaRetenido() {
        Kelitis kelitis = kelitisRetenido();

        kelitis.solicitarReembolso("Servicio cancelado");

        assertEquals(EstadoKelitis.REEMBOLSADO, kelitis.getEstado());
    }

    @Test
    void noPermitirReembolsoSinMotivo() {
        Kelitis kelitis = kelitisRetenido();

        assertThrows(ReglaDominioException.class, () -> kelitis.solicitarReembolso(""));

        assertEquals(EstadoKelitis.RETENIDO, kelitis.getEstado());
    }

    @Test
    void noPermitirReembolsoDeUnKelitisCompletado() {
        Kelitis kelitis = kelitisRetenido();
        kelitis.liberarPago();
        kelitis.completar();

        assertThrows(ReglaDominioException.class, () -> kelitis.solicitarReembolso("Quiero devolver el dinero"));

        assertEquals(EstadoKelitis.COMPLETADO, kelitis.getEstado());
    }
}
