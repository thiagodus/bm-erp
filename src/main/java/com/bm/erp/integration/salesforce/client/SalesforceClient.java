package com.bm.erp.integration.salesforce.client;

import com.bm.erp.integration.salesforce.dto.SFOpportunityUpdateRequest;
import com.bm.erp.integration.salesforce.service.SalesforceTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class SalesforceClient {

    private final RestClient restClient;
    private final SalesforceTokenProvider salesforceTokenProvider;
    private static final Logger log = LoggerFactory.getLogger(SalesforceClient.class);


    public SalesforceClient(RestClient.Builder builder,
                            SalesforceTokenProvider salesforceTokenProvider) {
        this.restClient = builder.build();
        this.salesforceTokenProvider = salesforceTokenProvider;

    }



    public void updateOpportunity(String opportunityId, SFOpportunityUpdateRequest request){

        try{
            executePatch(opportunityId, request);
        }catch(HttpClientErrorException.Unauthorized e){
            log.warn("Salesforce token unauthorized (401) for opportunity {}. Evicting cache and retrying once.", opportunityId);
            salesforceTokenProvider.evictToken();
            try{
                executePatch(opportunityId, request);
            }catch(HttpClientErrorException ex){
                log.error("Salesforce opportunity update failed after retry for opportunity: {}", opportunityId, ex);
                throw ex;
            }
        }
    }

    private void executePatch(String opportunityId, SFOpportunityUpdateRequest request) {
        var token = salesforceTokenProvider.getCachedToken();
        restClient.patch()
                .uri(token.instanceUrl() +"/services/data/v60.0/sobjects/Opportunity/"+ opportunityId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + token.accessToken()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
