package com.bm.erp.integration.salesforce.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "salesforce")
public record SalesforceProperties(String authUrl,
                                   String clientId,
                                   String clientSecret
                                   ) {
}
