package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeteEmbeddable {

    @Column(name = "sete_nombre", nullable = false, length = 100)
    private String nombre;
}
