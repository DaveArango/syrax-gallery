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
public class IndiorEmbeddable {

    @Column(name = "indior_descripcion", nullable = false, length = 200)
    private String descripcion;
}
