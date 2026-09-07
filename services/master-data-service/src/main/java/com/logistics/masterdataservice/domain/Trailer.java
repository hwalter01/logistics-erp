package com.logistics.masterdataservice.domain;

import com.logistics.masterdataservice.domain.base.BaseEntity;
import com.logistics.masterdataservice.domain.enums.TrailerStatus;
import com.logistics.masterdataservice.domain.enums.TrailerType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "trailers", schema = "md", uniqueConstraints = {
        @UniqueConstraint(name = "ux_trailers_trailer_number", columnNames = "trailer_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trailer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "trailer_id", nullable = false, updatable = false)
    private UUID trailerId;

    @PrePersist
    void prePersist() {
        if (trailerId == null) trailerId = UUID.randomUUID();
    }

    @Column(name = "trailer_number", nullable = false, updatable = false)
    private String trailerNumber;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "trailer_type")
    private TrailerType trailerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TrailerStatus status;

    @Column(name = "notes")
    private String notes;


}
