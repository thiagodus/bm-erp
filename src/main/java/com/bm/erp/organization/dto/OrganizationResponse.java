package com.bm.erp.organization.dto;

import java.util.Optional;

public record OrganizationResponse(

        String legalName,
        String tradeName,
        String cnpj,
        String email,
        String phone,
        String street,
        String city,
        String state,
        String zipCode,
        String country
) {



}
