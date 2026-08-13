package com.bm.erp.integration.nfe.dto;

import java.math.BigDecimal;

public record NfeRequest(String customerName,
                         String description,
                         BigDecimal total) {
}
