package com.bm.erp.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record OrderRequest(

        UUID customerId,

        @Size(max = 500)
        String notes,

        @NotEmpty
        List<OrderItemRequest> items
) {
}
