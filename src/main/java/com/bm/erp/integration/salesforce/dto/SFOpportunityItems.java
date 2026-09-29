package com.bm.erp.integration.salesforce.dto;

import java.math.BigDecimal;

public record SFOpportunityItems(
        String sku,
        int quantity,
        BigDecimal unitPrice
) {
}
