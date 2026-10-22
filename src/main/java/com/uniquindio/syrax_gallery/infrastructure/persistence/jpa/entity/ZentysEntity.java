package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import com.uniquindio.syrax_gallery.domain.valueobject.Runiapos;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "zentys")
@Getter
@Setter
@NoArgsConstructor
public class ZentysEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Embedded
    private RuniaposEmbeddable runiapos;

    @Embedded
    private IndiorEmbeddable indior;

    @Column(nullable = false)
    private boolean estaActivo;
}
