package com.bm.erp.order.event;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        String customerName,
        BigDecimal total
) {}