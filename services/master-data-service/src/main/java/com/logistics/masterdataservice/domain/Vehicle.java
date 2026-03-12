package com.logistics.masterdataservice.domain;

import com.logistics.masterdataservice.domain.base.BaseEntity;
import com.logistics.masterdataservice.domain.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "vehicles", schema = "md", uniqueConstraints = {
        @UniqueConstraint(name = "ux_vehicles_vehicle_number", columnNames = "vehicle_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "vehicle_id", nullable = false, updatable = false)
    private UUID vehicleId;

    @PrePersist
    void prePersist() {
        if (vehicleId == null) vehicleId = UUID.randomUUID();
    }

    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "vin")
    private String vin;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VehicleStatus status;

    @Column(name = "notes")
    private String notes;


}
