package com.bm.erp.integration.boleto;

import com.bm.erp.boleto.dto.BoletoRequest;
import com.bm.erp.integration.boleto.client.WebhookBoletoClient;
import com.bm.erp.integration.boleto.dto.FBoletoResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FakeBoletoProvider {
    private final Map<String, FBoletoResponse> processedRequests = new ConcurrentHashMap<>();
    private String externalId;
    private final WebhookBoletoClient webhookBoletoClient;

    FakeBoletoProvider(WebhookBoletoClient webhookBoletoClient) {
        this.webhookBoletoClient = webhookBoletoClient;
    }


    public FBoletoResponse createBoleto(String idempotencyKey, BoletoRequest boletoRequest){

        FBoletoResponse boletoResponse = processedRequests.computeIfAbsent(idempotencyKey,
                k -> new FBoletoResponse(UUID.randomUUID().toString(), "OPEN"));

        externalId = boletoResponse.externalId();

        return boletoResponse;
    }

    public void simulatePayment(String externalId, String status){
        webhookBoletoClient.sendWebhook(externalId, status);
    }
}
