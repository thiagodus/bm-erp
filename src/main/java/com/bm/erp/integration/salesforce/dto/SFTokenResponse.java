package com.bm.erp.integration.salesforce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SFTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("instance_url") String instanceUrl,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("issued_at") Long issuedAt
) {
}
