package com.bm.erp.integration.salesforce.controller;

import com.bm.erp.integration.salesforce.dto.SFOpportunityWebhookRequest;
import com.bm.erp.integration.salesforce.service.SalesforceSyncService;
import com.bm.erp.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integrations/salesforce/opportunity-won")
public class SalesforceIntegrationController {

    private final SalesforceSyncService salesforceSyncService;

    public  SalesforceIntegrationController(SalesforceSyncService salesforceSyncService) {
        this.salesforceSyncService = salesforceSyncService;
    }

    @PostMapping
    public OrderResponse receiveOrder(@RequestBody @Valid SFOpportunityWebhookRequest request){
        return salesforceSyncService.syncOpportunityWon(request);
    }
}
