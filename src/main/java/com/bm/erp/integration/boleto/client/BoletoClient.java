package com.bm.erp.integration.boleto.client;

import com.bm.erp.integration.boleto.exception.BoletoIntegrationException;
import com.bm.erp.boleto.dto.BoletoRequest;
import com.bm.erp.boleto.dto.BoletoResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class BoletoClient {

    private final RestClient restClient;
    public BoletoClient(@Qualifier("boletoRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public BoletoResponse createBoleto(BoletoRequest request, String idempotencyKey) throws BoletoIntegrationException {
        try {
            return restClient
                    .post()
                    .uri("/test-provider/boletos")
                    .header("Idempotency-Key",  idempotencyKey)
                    .body(request)
                    .retrieve()
                    .body(BoletoResponse.class);

        } catch (RestClientException e) {
            throw new BoletoIntegrationException(
                    "Failed to communicate with boleto provider",
                    e
            );
        }
    }

}
