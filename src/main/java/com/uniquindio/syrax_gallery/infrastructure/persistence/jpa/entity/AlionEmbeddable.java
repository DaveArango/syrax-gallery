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
public class AlionEmbeddable {

    @Column(name = "alion_ciudad", nullable = false, length = 100)
    private String ciudad;

    @Column(name = "alion_pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "alion_latitud", nullable = false)
    private double latitud;

    @Column(name = "alion_longitud", nullable = false)
    private double longitud;
}
