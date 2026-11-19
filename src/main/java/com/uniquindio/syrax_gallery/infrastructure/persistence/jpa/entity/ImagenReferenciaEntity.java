package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import com.uniquindio.syrax_gallery.domain.valueobject.TipoImagenReferencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagenes_referencia")
@Getter
@Setter
@NoArgsConstructor
public class ImagenReferenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoImagenReferencia tipo;

    @Column(name = "id_propietario", nullable = false, length = 36)
    private String idPropietario;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    private int orden;
}