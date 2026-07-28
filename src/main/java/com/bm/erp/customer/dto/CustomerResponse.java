package com.bm.erp.customer.dto;

import com.bm.erp.customer.entity.CustomerType;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        CustomerType type,
        String name,
        String phone,
        String document,
        String email,
        String street,
        String city,
        String state,
        String zipCode,
        String country,
        String notes,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
