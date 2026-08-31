package com.bm.erp.integration.boleto.client;

import com.bm.erp.integration.boleto.dto.WebhookRequest;
import com.bm.erp.integration.boleto.exception.BoletoIntegrationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class WebhookBoletoClient {
    private final RestClient restClient;

    public WebhookBoletoClient(@Qualifier("webhookBoletoRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendWebhook(String externalId, String status) {
        try {
                restClient
                    .post()
                    .uri("/webhooks/boletos")
                    .body(new WebhookRequest(externalId, status))
                        .retrieve()
                        .toBodilessEntity();
        } catch (RestClientException e) {
            throw new BoletoIntegrationException(
                    "Failed to communicate with the ERP from the webhook service",
                    e
            );
        }
    }
}
