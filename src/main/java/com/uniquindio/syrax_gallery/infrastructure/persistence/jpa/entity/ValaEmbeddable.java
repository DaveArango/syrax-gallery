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
public class ValaEmbeddable {

    @Column(name = "vala_monto", nullable = false)
    private double monto;

    @Column(name = "vala_divisa", nullable = false, length = 3)
    private String divisa;
}
