package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import com.uniquindio.syrax_gallery.domain.valueobject.SindesJeda;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "sindes")
@Getter @Setter
@NoArgsConstructor
public class SindesEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "id_caraxes", nullable = false, length = 36)
    private String idCaraxes;

    @Column(name = "id_azantys", nullable = false, length = 36)
    private String idAzantys;

    @Column(name = "id_zentys", nullable = false, length = 36)
    private String idZentys;

    @Embedded
    private ValaEmbeddable valaCongelado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "sindes_jeda", nullable = false, length = 20)
    private SindesJeda sindesJeda;
}
