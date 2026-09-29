package com.bm.erp.integration.salesforce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SFOpportunityUpdateRequest(
        @JsonProperty("StageName") String stageName,
        @JsonProperty("Description") String description) {
}
