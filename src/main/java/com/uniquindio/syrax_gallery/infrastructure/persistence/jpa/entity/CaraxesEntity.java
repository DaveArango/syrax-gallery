package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import com.uniquindio.syrax_gallery.domain.valueobject.CaraxesKastor;
import com.uniquindio.syrax_gallery.domain.valueobject.Kanez;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "caraxes")
@Getter
@Setter
@NoArgsConstructor
public class CaraxesEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "id_azantys", nullable = false, length = 50)
    private String idAzantys;

    @Embedded
    private ValaEmbeddable vala;

    @Embedded
    private SeteEmbeddable sete;

    @Embedded
    private JorvaEmbeddable jorva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Kanez kanez;

    @Embedded
    private UrnebionEmbeddable urnebion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CaraxesKastor kastor;
}
