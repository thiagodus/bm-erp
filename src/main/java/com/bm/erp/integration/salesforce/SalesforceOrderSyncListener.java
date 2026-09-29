package com.bm.erp.integration.salesforce;

import com.bm.erp.integration.salesforce.client.SalesforceClient;
import com.bm.erp.integration.salesforce.dto.SFOpportunityUpdateRequest;
import com.bm.erp.order.event.OrderPaidEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SalesforceOrderSyncListener {
    private final SalesforceClient salesforceClient;

    public SalesforceOrderSyncListener(SalesforceClient salesforceClient) {
        this.salesforceClient = salesforceClient;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderPaid(OrderPaidEvent event) {
        if (event.externalId() == null || event.externalId().isBlank()) {
            return;
        }

        System.out.println(">>> Syncing paid status to Salesforce for Opportunity: " + event.externalId());

        salesforceClient.updateOpportunity(event.externalId(),
                new SFOpportunityUpdateRequest("Closed Won",
                        "Payment confiremed via Boleto in ERP"));
    }
}
