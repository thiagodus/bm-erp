package com.bm.erp.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrganizationRequest(
        @NotBlank
        String legalName,

        @NotBlank
        String tradeName,

        @NotBlank
        String cnpj,

        @Email
        @NotBlank
        String email,

        String phone,

        String street,
        String city,
        String state,
        String zipCode,
        String country
) {
}
