package com.bm.erp.integration.salesforce.dto;

import java.math.BigDecimal;
import java.util.List;

public record SFOpportunityWebhookRequest(
        String opportunityId,
        String accountName,
        String accountTaxId,
        List<SFOpportunityItems> items,
        BigDecimal totalItems
) {
}
