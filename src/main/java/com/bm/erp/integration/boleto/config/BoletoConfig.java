package com.bm.erp.integration.boleto.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BoletoConfig {
    @Bean
    @Qualifier("boletoRestClient")
    RestClient boletoRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Bean
    @Qualifier("webhookBoletoRestClient")
    RestClient webhookBoletoRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

}
