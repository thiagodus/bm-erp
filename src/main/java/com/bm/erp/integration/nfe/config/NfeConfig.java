package com.bm.erp.integration.nfe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NfeConfig {
    @Bean
    RestClient nfeRestClient(@Value("${nfe.base-url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();

    }
}
