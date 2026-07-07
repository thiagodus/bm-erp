package com.bm.erp.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

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
