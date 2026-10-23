package com.uniquindio.syrax_gallery.infrastructure.persistence.jpa.entity;

import com.uniquindio.syrax_gallery.domain.valueobject.Gelior;
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

@Entity
@Table(name = "kelitis")
@Getter @Setter
@NoArgsConstructor
public class KelitisEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "id_sunfyre", nullable = false, unique = true, length = 36)
    private String idSunfyre;

    @Column(name = "id_azantys", nullable = false, length = 36)
    private String idAzantys;

    @Column(name = "id_zentys", nullable = false, length = 36)
    private String idZentys;

    @Embedded
    private ValaEmbeddable valaCongelado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gelior gelior;
}
