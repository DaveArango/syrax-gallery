package com.uniquindio.syrax_gallery.domain.valueobject;

/**
 * Estados de un Sindes (compra de un Caraxes).
 * Las transiciones válidas viven aquí y no en ifs regados por el código.
 */
public enum SindesJeda {
    PENDIENTE,
    COMPLETADA,
    CANCELADA;

    public boolean puedeTransicionarA(SindesJeda siguiente) {
        return switch (this) {
            case PENDIENTE -> siguiente == COMPLETADA || siguiente == CANCELADA;
            case COMPLETADA, CANCELADA -> false; // estados finales
        };
    }

    public boolean esFinal() {
        return this != PENDIENTE;
    }
}
