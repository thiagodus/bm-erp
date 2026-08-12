package com.bm.erp.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequest(
        @NotNull
        UUID productId,

        @NotNull
        @Positive
        Integer quantity,

        @NotNull
        @PositiveOrZero
        BigDecimal unitPrice,

        @Size(max = 500)
        String notes) {
}
