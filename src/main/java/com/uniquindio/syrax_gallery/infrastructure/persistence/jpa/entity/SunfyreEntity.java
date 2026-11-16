package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import com.uniquindio.syrax_gallery.domain.valueobject.SunfyreKastor;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "sunfyre")
@Getter @Setter
@NoArgsConstructor
public class SunfyreEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "id_azantys", nullable = false, length = 50)
    private String idAzantys;

    @Embedded
    private SeteEmbeddable sete;

    @Embedded
    private JorvaEmbeddable jorva;

    @Embedded
    private UrnebionEmbeddable urnebion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SunfyreKastor kastor;

    @Embedded
    private AlionEmbeddable alion;

    @Embedded
    private ValaEmbeddable vala;

    @Column(name = "duracion_minutos", nullable = false)
    private int duracionMinutos;

    // Días de atención separados por coma: MONDAY,TUESDAY,WEDNESDAY
    @Column(nullable = false, length = 80)
    private String dias;

    @Column(nullable = false)
    private LocalTime apertura;

    @Column(nullable = false)
    private LocalTime cierre;

    @Column(nullable = false)
    private boolean activo;
}
