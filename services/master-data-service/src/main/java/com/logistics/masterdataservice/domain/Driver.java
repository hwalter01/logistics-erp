package com.logistics.masterdataservice.domain;

import com.logistics.masterdataservice.domain.base.BaseEntity;
import com.logistics.masterdataservice.domain.enums.EmploymentType;
import com.logistics.masterdataservice.domain.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "drivers", schema = "md", uniqueConstraints = {
        @UniqueConstraint(name = "ux_drivers_driver_number", columnNames = "driver_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "driver_id", nullable = false, updatable = false)
    private UUID driverId;

    @PrePersist
    void prePersist() {
        if (driverId == null) driverId = UUID.randomUUID();
    }

    @Column(name = "driver_number", nullable = false, updatable = false)
    private String driverNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriverStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
}
