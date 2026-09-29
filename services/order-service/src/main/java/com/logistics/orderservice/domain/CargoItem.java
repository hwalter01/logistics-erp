package com.logistics.orderservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cargo_items", schema = "ord")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "cargo_item_id",
            nullable = false,
            updatable = false
    )
    private UUID cargoItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private TransportOrder transportOrder;

    @Builder.Default
    @OneToMany(
            mappedBy = "cargoItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CargoMeasurement> cargoMeasurements = new ArrayList<>();

    @Column(
            name = "description",
            nullable = false
    )
    private String description;

    @Column(name = "notes")
    private String notes;

    public void addMeasurement(CargoMeasurement measurement) {
        cargoMeasurements.add(measurement);
        measurement.setCargoItem(this);
    }
}
