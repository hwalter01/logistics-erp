package com.logistics.masterdataservice.domain;

import com.logistics.masterdataservice.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customers", schema = "md", uniqueConstraints = {
        @UniqueConstraint(name = "ux_customers_customer_number", columnNames = "customer_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id", nullable = false, updatable = false)
    private UUID customerId;

    @PrePersist
    void prePersist() {
        if (customerId == null) customerId = UUID.randomUUID();
    }

    @Column(name = "customer_number", nullable = false, updatable = false)
    private String customerNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "vat_number")
    private String vatNumber;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "notes")
    private String notes;

    @Column(name = "pod_required", nullable = false)
    private boolean podRequired ;
}
