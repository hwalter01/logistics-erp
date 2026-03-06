package com.logistics.masterdataservice.domain;

import com.logistics.masterdataservice.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses", schema = "md")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {
    @Id
    @Column(name = "address_id", nullable = false, updatable = false)
    private UUID addressId;

    @PrePersist
    void prePersist() {
        if (addressId == null) addressId = UUID.randomUUID();
    }

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "additional_line")
    private String additionalLine;
}