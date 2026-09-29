package com.logistics.orderservice.domain;

import com.logistics.orderservice.domain.enums.StopType;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_stops", schema = "ord")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStop extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "stop_id",
            nullable = false,
            updatable = false
    )
    private UUID stopId;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private TransportOrder transportOrder;

    @Column(
            name = "sequence_number",
            nullable = false
    )
    private int sequenceNumber;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "stop_type",
            nullable = false
    )
    private StopType type;

    @Column(
            name = "location_id",
            nullable = false
    )
    private UUID locationId;

    @Column(
            name = "time_window_start"
    )
    private Instant timeWindowStart;

    @Column(
            name = "time_window_end"
    )
    private Instant timeWindowEnd;

    @Column(
            name = "instructions"
    )
    private String instructions;

    @Column(
            name = "reference"
    )
    private String reference;
}
