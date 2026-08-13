package com.bm.erp.integration.nfe.dto;

import java.math.BigDecimal;

public record NfeResponse(
        Long id,
        String customerName,
        String description,
        BigDecimal total
) {
}
