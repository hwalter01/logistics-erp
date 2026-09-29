package com.logistics.orderservice.domain;

import com.logistics.orderservice.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "orders",
        schema = "ord",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_orders_order_number", columnNames = "order_number"),
                @UniqueConstraint(name = "ux_orders_customer_reference", columnNames = "customer_id"),
                @UniqueConstraint(name = "ux_orders_customer_reference", columnNames = "customer_reference")
        })

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportOrder extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "order_id",
            nullable = false,
            updatable = false
    )
    private UUID orderId;

    @Column(
            name = "order_number",
            nullable = false,
            updatable = false
    )
    private String orderNumber;

    @Builder.Default
    @OneToMany(
            mappedBy = "transportOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("sequenceNumber ASC")
    private List<OrderStop> stops = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "transportOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CargoItem> cargoItems= new ArrayList<>();

    @Column(
            name = "customer_id",
            nullable = false
    )
    private UUID customerId;

    @Column(
            name = "customer_reference",
            length = 100
    )
    private String customerReference;

    @Column(
            name = "special_requirements"
    )
    private String specialRequirements;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private OrderStatus status;

    public void addStop(OrderStop stop) {
        stops.add(stop);
        stop.setTransportOrder(this);
    }

    public void addCargoItem(CargoItem cargoItem) {
        cargoItems.add(cargoItem);
        cargoItem.setTransportOrder(this);
    }
}
