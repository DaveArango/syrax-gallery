package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "azantys")
@Getter @Setter
@NoArgsConstructor
public class AzantysEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private boolean activo;

    @Column(nullable = false, length = 100)
    private String kostion;

    @Column(nullable = false, length = 150)
    private String runiapos;

    @Column(length = 10)
    private String iksia;

    @Column(name = "iksia_verificada", nullable = false)
    private boolean iksiaVerificada;

    @Column(length = 500)
    private String laehurlion;

    @Column(name = "laehurlion_verificado", nullable = false)
    private boolean laehurlionVerificado;

    @Column(name = "identidad_aprobada", nullable = false)
    private boolean identidadAprobada;
}
