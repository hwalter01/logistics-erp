package com.logistics.orderservice.domain;

import com.logistics.orderservice.domain.enums.CargoUnit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cargo_measurements", schema = "ord")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoMeasurement extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "measurement_id",
            nullable = false,
            updatable = false
    )
    private UUID measurementId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "cargo_item_id",
            nullable = false
    )
    private CargoItem cargoItem;

    @Column(
            name = "measurement_value",
            nullable = false,
            precision = 15,
            scale = 3
    )
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "unit",
            nullable = false
    )
    private CargoUnit unit;

}
