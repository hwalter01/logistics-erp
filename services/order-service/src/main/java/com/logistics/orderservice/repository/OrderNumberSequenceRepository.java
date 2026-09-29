package com.logistics.orderservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderNumberSequenceRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String NEXT_NUMBER_SQL = """
            INSERT INTO ord.order_number_sequences (
                customer_id,
                year,
                last_number
            )
            VALUES (
                :customerId,
                :year,
                1
            )
            ON CONFLICT (customer_id, year)
            DO UPDATE
            SET last_number = ord.order_number_sequences.last_number + 1
            RETURNING last_number
            """;

    public int nextNumber(UUID customerId, int year) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", customerId)
                .addValue("year", year);

        Integer nextNumber = jdbcTemplate.queryForObject(
                NEXT_NUMBER_SQL,
                params,
                Integer.class
        );

        if (nextNumber == null) {
            throw new IllegalStateException(
                    "Could not generate next order number"
            );
        }

        return nextNumber;
    }
}
