package com.uniquindio.syrax_gallery.domain.entity;

import com.uniquindio.syrax_gallery.domain.exception.ReglaDominioException;
import com.uniquindio.syrax_gallery.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CaraxesTest {

    private static final String ID = "caraxes-1";
    private static final String AZANTYS_ID = "azantys-1";

    private Vala precio() {
        return new Vala(1200.00, "USD");
    }

    private Sete sete() {
        return new Sete("Noche Estrellada");
    }

    private Jorva jorva() {
        return new Jorva("Paisaje al atardecer pintado con técnica de óleo");
    }

    private Urnebion urnebion() {
        return new Urnebion("https://cdn.syrax.com/obras/paisaje.jpg");
    }

    private Caraxes crearCaraxes() {
        return Caraxes.crear(ID, AZANTYS_ID, precio(), sete(), jorva(), urnebion(), CaraxesKastor.PINTURA);
    }

    private Caraxes crearCaraxesConPrecio(double monto) {
        return Caraxes.crear(ID, AZANTYS_ID, new Vala(monto, "USD"), sete(), jorva(), urnebion(),
                CaraxesKastor.PINTURA);
    }

    @Test
    void testCrearCaraxesValido() {
        Vala vala = precio();
        Sete sete = sete();
        Jorva jorva = jorva();
        Urnebion urnebion = urnebion();

        Caraxes caraxes = Caraxes.crear(ID, AZANTYS_ID, vala, sete, jorva, urnebion, CaraxesKastor.PINTURA);

        assertNotNull(caraxes);
        assertEquals(ID, caraxes.getId());
        assertEquals(AZANTYS_ID, caraxes.getAzantysId());
        assertEquals(vala, caraxes.getVala());
        assertEquals(sete, caraxes.getSete());
        assertEquals(jorva, caraxes.getJorva());
        assertEquals(urnebion, caraxes.getUrnebion());
        assertEquals(CaraxesKastor.PINTURA, caraxes.getKastor());
    }

    @Test
    void testCrearCaraxesNaceEnBorrador() {
        Caraxes caraxes = crearCaraxes();

        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }

    @Test
    void testCrearCaraxesConIdNulo() {
        String id = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(id, AZANTYS_ID, precio(), sete(), jorva(), urnebion(), CaraxesKastor.PINTURA));

        assertEquals("El id del Caraxes es obligatorio", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConIdVacio() {
        String id = "   ";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(id, AZANTYS_ID, precio(), sete(), jorva(), urnebion(), CaraxesKastor.PINTURA));

        assertEquals("El id del Caraxes es obligatorio", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConAzantysIdNulo() {
        String azantysId = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(ID, azantysId, precio(), sete(), jorva(), urnebion(), CaraxesKastor.PINTURA));

        assertEquals("El id del Azantys es obligatorio", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConAzantysIdVacio() {
        String azantysId = "";

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(ID, azantysId, precio(), sete(), jorva(), urnebion(), CaraxesKastor.PINTURA));

        assertEquals("El id del Azantys es obligatorio", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConValaNulo() {
        Vala vala = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(ID, AZANTYS_ID, vala, sete(), jorva(), urnebion(), CaraxesKastor.PINTURA));

        assertEquals("El Vala no puede ser nulo.", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConSeteNulo() {
        Sete sete = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(ID, AZANTYS_ID, precio(), sete, jorva(), urnebion(), CaraxesKastor.PINTURA));

        assertEquals("El Sete no puede ser nulo.", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConJorvaNulo() {
        Jorva jorva = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(ID, AZANTYS_ID, precio(), sete(), jorva, urnebion(), CaraxesKastor.PINTURA));

        assertEquals("El Jorva no puede ser nulo.", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConUrnebionNulo() {
        Urnebion urnebion = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(ID, AZANTYS_ID, precio(), sete(), jorva(), urnebion, CaraxesKastor.PINTURA));

        assertEquals("El Urnebion no puede ser nulo.", ex.getMessage());
    }

    @Test
    void testCrearCaraxesConKastorNulo() {

        CaraxesKastor kastor = null;

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> Caraxes.crear(ID, AZANTYS_ID, precio(), sete(), jorva(), urnebion(), kastor));

        assertEquals("El Kastor no puede ser nulo.", ex.getMessage());
    }



    @Test
    void testPublicarParaExhibicionDesdeBorrador() {
        Caraxes caraxes = crearCaraxes();

        caraxes.publicarParaExhibicion();

        assertEquals(Kanez.EXHIBICION, caraxes.getKanez());
    }

    @Test
    void testPublicarParaExhibicionDesdeExhibicion() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaExhibicion();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::publicarParaExhibicion);

        assertEquals("Solo un Caraxes en BORRADOR puede publicarse para exhibición", ex.getMessage());
        assertEquals(Kanez.EXHIBICION, caraxes.getKanez());
    }

    @Test
    void testPublicarParaExhibicionDesdeEnVenta() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaVenta();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::publicarParaExhibicion);

        assertEquals("Solo un Caraxes en BORRADOR puede publicarse para exhibición", ex.getMessage());
        assertEquals(Kanez.EN_VENTA, caraxes.getKanez());
    }

    @Test
    void testPublicarParaExhibicionDesdeRetirado() {
        Caraxes caraxes = crearCaraxes();
        caraxes.retirar();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::publicarParaExhibicion);

        assertEquals("Solo un Caraxes en BORRADOR puede publicarse para exhibición", ex.getMessage());
        assertEquals(Kanez.RETIRADO, caraxes.getKanez());
    }



    @Test
    void testPublicarParaVentaDesdeBorrador() {
        Caraxes caraxes = crearCaraxes();

        caraxes.publicarParaVenta();

        assertEquals(Kanez.EN_VENTA, caraxes.getKanez());
    }

    @Test
    void testPublicarParaVentaDesdeExhibicion() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaExhibicion();

        caraxes.publicarParaVenta();

        assertEquals(Kanez.EN_VENTA, caraxes.getKanez());
    }

    @Test
    void testPublicarParaVentaConPrecioCero() {
        Caraxes caraxes = crearCaraxesConPrecio(0);

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::publicarParaVenta);

        assertEquals("Un Caraxes debe tener un precio mayor a cero para ponerse en venta", ex.getMessage());
        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }

    @Test
    void testPublicarParaVentaDesdeEnVenta() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaVenta();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::publicarParaVenta);

        assertEquals("Solo un Caraxes en BORRADOR o EXHIBICION puede ponerse en venta", ex.getMessage());
        assertEquals(Kanez.EN_VENTA, caraxes.getKanez());
    }

    @Test
    void testPublicarParaVentaDesdeVendido() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaVenta();
        caraxes.marcarComoVendido();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::publicarParaVenta);

        assertEquals("Solo un Caraxes en BORRADOR o EXHIBICION puede ponerse en venta", ex.getMessage());
        assertEquals(Kanez.VENDIDO, caraxes.getKanez());
    }

    @Test
    void testPublicarParaVentaDesdeRetirado() {
        Caraxes caraxes = crearCaraxes();
        caraxes.retirar();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::publicarParaVenta);

        assertEquals("Solo un Caraxes en BORRADOR o EXHIBICION puede ponerse en venta", ex.getMessage());
        assertEquals(Kanez.RETIRADO, caraxes.getKanez());
    }



    @Test
    void testMarcarComoVendidoDesdeEnVenta() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaVenta();

        caraxes.marcarComoVendido();

        assertEquals(Kanez.VENDIDO, caraxes.getKanez());
    }

    @Test
    void testMarcarComoVendidoDesdeBorrador() {
        Caraxes caraxes = crearCaraxes();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::marcarComoVendido);

        assertEquals("Solo un Caraxes EN_VENTA puede marcarse como vendido", ex.getMessage());
        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }

    @Test
    void testMarcarComoVendidoDesdeExhibicion() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaExhibicion();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::marcarComoVendido);

        assertEquals("Solo un Caraxes EN_VENTA puede marcarse como vendido", ex.getMessage());
        assertEquals(Kanez.EXHIBICION, caraxes.getKanez());
    }

    @Test
    void testMarcarComoVendidoDesdeVendido() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaVenta();
        caraxes.marcarComoVendido();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::marcarComoVendido);

        assertEquals("Solo un Caraxes EN_VENTA puede marcarse como vendido", ex.getMessage());
        assertEquals(Kanez.VENDIDO, caraxes.getKanez());
    }


    @Test
    void testRetirarDesdeBorrador() {
        Caraxes caraxes = crearCaraxes();

        caraxes.retirar();

        assertEquals(Kanez.RETIRADO, caraxes.getKanez());
    }

    @Test
    void testRetirarDesdeExhibicion() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaExhibicion();

        caraxes.retirar();

        assertEquals(Kanez.RETIRADO, caraxes.getKanez());
    }

    @Test
    void testRetirarDesdeEnVenta() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaVenta();

        caraxes.retirar();

        assertEquals(Kanez.RETIRADO, caraxes.getKanez());
    }

    @Test
    void testRetirarDesdeVendido() {
        Caraxes caraxes = crearCaraxes();
        caraxes.publicarParaVenta();
        caraxes.marcarComoVendido();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::retirar);

        assertEquals("Un Caraxes VENDIDO no puede retirarse", ex.getMessage());
        assertEquals(Kanez.VENDIDO, caraxes.getKanez());
    }

    @Test
    void testRetirarDesdeRetirado() {
        Caraxes caraxes = crearCaraxes();
        caraxes.retirar();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, caraxes::retirar);

        assertEquals("El Caraxes ya se encuentra RETIRADO", ex.getMessage());
        assertEquals(Kanez.RETIRADO, caraxes.getKanez());
    }



    @Test
    void testPublicarConDestinoNulo() {
        Caraxes caraxes = crearCaraxes();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class, () -> caraxes.publicar(null));

        assertEquals("El destino de publicación es obligatorio", ex.getMessage());
        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }

    @Test
    void testPublicarConDestinoExhibicion() {
        Caraxes caraxes = crearCaraxes();

        caraxes.publicar(Kanez.EXHIBICION);

        assertEquals(Kanez.EXHIBICION, caraxes.getKanez());
    }

    @Test
    void testPublicarConDestinoEnVenta() {
        Caraxes caraxes = crearCaraxes();

        caraxes.publicar(Kanez.EN_VENTA);

        assertEquals(Kanez.EN_VENTA, caraxes.getKanez());
    }

    @Test
    void testPublicarConDestinoBorrador() {

        Caraxes caraxes = crearCaraxes();


        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> caraxes.publicar(Kanez.BORRADOR));

        assertEquals("Solo se puede publicar para EXHIBICION o EN_VENTA", ex.getMessage());
        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }

    @Test
    void testPublicarConDestinoVendido() {
        Caraxes caraxes = crearCaraxes();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> caraxes.publicar(Kanez.VENDIDO));

        assertEquals("Solo se puede publicar para EXHIBICION o EN_VENTA", ex.getMessage());
        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }

    @Test
    void testPublicarConDestinoRetirado() {
        Caraxes caraxes = crearCaraxes();

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> caraxes.publicar(Kanez.RETIRADO));

        assertEquals("Solo se puede publicar para EXHIBICION o EN_VENTA", ex.getMessage());
        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }

    @Test
    void testPublicarConDestinoEnVentaYPrecioCero() {
        Caraxes caraxes = crearCaraxesConPrecio(0);

        ReglaDominioException ex = assertThrows(ReglaDominioException.class,
                () -> caraxes.publicar(Kanez.EN_VENTA));

        assertEquals("Un Caraxes debe tener un precio mayor a cero para ponerse en venta", ex.getMessage());
        assertEquals(Kanez.BORRADOR, caraxes.getKanez());
    }



    @Test
    void testEqualsConElMismoId() {
        Caraxes primero = crearCaraxes();
        Caraxes segundo = Caraxes.crear(ID, "otro-azantys", new Vala(50, "COP"), new Sete("Otra Obra"),
                new Jorva("Descripción completamente distinta"), new Urnebion("https://cdn.syrax.com/otra.jpg"),
                CaraxesKastor.ESCULTURA);

        boolean iguales = primero.equals(segundo);

        assertTrue(iguales);
        assertEquals(primero.hashCode(), segundo.hashCode());
    }

    @Test
    void testEqualsConDistintoId() {
        Caraxes primero = crearCaraxes();
        Caraxes segundo = Caraxes.crear("caraxes-2", AZANTYS_ID, precio(), sete(), jorva(), urnebion(),
                CaraxesKastor.PINTURA);

        boolean iguales = primero.equals(segundo);

        assertFalse(iguales);
    }

    @Test
    void testEqualsConNuloYOtroTipo() {
        Caraxes caraxes = crearCaraxes();

        boolean contraNulo = caraxes.equals(null);
        boolean contraOtroTipo = caraxes.equals("caraxes-1");

        assertFalse(contraNulo);
        assertFalse(contraOtroTipo);
    }

    @Test
    void testEqualsConSigoMismo() {
        Caraxes caraxes = crearCaraxes();

        boolean iguales = caraxes.equals(caraxes);

        assertTrue(iguales);
    }
}
