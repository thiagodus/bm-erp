package com.bm.erp.order.dto;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public record OrderUpdateRequest(
        UUID customerId,
        String notes,
        List<@Valid OrderItemRequest> items
) {
}