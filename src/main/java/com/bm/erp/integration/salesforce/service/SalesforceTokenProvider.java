package com.bm.erp.integration.salesforce.service;

import com.bm.erp.integration.salesforce.config.SalesforceProperties;
import com.bm.erp.integration.salesforce.dto.SFTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;

@Service
public class SalesforceTokenProvider {

    private final RestClient restClient;
    private final SalesforceProperties salesforceProperties;

    public SalesforceTokenProvider(RestClient.Builder builder, SalesforceProperties salesforceProperties) {
        this.restClient = builder.build();
        this.salesforceProperties = salesforceProperties;
    }


    public record CachedToken(String accessToken, String instanceUrl, Instant expiresAt){
        public boolean isValid(){
            return expiresAt != null && Instant.now().isBefore(expiresAt.minusSeconds(60));
        }
    };
    private CachedToken cachedToken;

    public synchronized CachedToken getCachedToken(){
        if (this.cachedToken != null && this.cachedToken.isValid()){
            return this.cachedToken;
        }

        SFTokenResponse response = fetchTokenFromSalesforce();

        //add 1 hour
        Instant expiresAt = Instant.now().plusSeconds(3600);

        this.cachedToken = new CachedToken(response.accessToken(), response.instanceUrl(), expiresAt);
        return this.cachedToken;

    }

    private SFTokenResponse fetchTokenFromSalesforce() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", salesforceProperties.clientId());
        form.add("client_secret", salesforceProperties.clientSecret());

        return restClient.post()
                .uri(salesforceProperties.authUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(SFTokenResponse.class);
    }


    public String getAccessToken(){
        return getCachedToken().accessToken();
    }

    public String getInstanceUrl(){
        return getCachedToken().instanceUrl();
    }

    public synchronized void evictToken() {
        this.cachedToken = null;
    }


}
