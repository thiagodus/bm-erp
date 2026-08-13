package com.bm.erp.integration.nfe;

import com.bm.erp.integration.nfe.dto.NfeRequest;
import com.bm.erp.integration.nfe.dto.NfeResponse;
import com.bm.erp.integration.nfe.exception.NfeIntegrationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class NfeClient {
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

    public NfeResponse issueInvoice(NfeRequest request) {

        try {
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
