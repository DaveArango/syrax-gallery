package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "gida")
@Getter @Setter
@NoArgsConstructor
public class GidaEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "id_zentys", nullable = false, length = 36)
    private String idZentys;

    @Column(name = "id_caraxes", nullable = false, length = 36)
    private String idCaraxes;

    @Column(nullable = false, length = 500)
    private String texto;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
