package com.bm.erp.order.event;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderPaidEvent(
        UUID orderId,
        String externalId,
        BigDecimal totalAmount
) {
}
