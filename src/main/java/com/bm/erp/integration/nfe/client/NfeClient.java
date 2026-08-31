package com.bm.erp.integration.nfe.client;

import com.bm.erp.integration.nfe.NfeProvider;
import com.bm.erp.integration.nfe.dto.NfeRequest;
import com.bm.erp.integration.nfe.dto.NfeResponse;
import com.bm.erp.integration.nfe.exception.NfeIntegrationException;
import com.bm.erp.order.entity.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class NfeClient implements NfeProvider {
    private final RestClient restClient;
    public NfeClient(RestClient nfeRestClient) {
        this.restClient = nfeRestClient;
    }

    public String testConnection(){
        try{
            return restClient.get().uri("/posts/1").retrieve()
                    .body(String.class);
        }catch(RestClientException e){
            throw new NfeIntegrationException( "Failed to communicate with NFe provider",
                    e);
        }
    }

    public NfeResponse issueInvoice(Order order) {

        try {

            NfeRequest request = new NfeRequest(
                    order.getCustomer().getName(),
                    "Order " + order.getId(),
                    order.getTotal());

            return restClient
                    .post()
                    .uri("/posts")
                    .body(request)
                    .retrieve()
                    .body(NfeResponse.class);

        } catch (RestClientException e) {
            throw new NfeIntegrationException(
                    "Failed to communicate with NFe provider",
                    e
            );
        }
    }


}
