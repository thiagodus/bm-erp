package com.bm.erp.customer.dto;

import com.bm.erp.customer.entity.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotNull
        CustomerType type,

        @NotBlank
        String name,

        @NotBlank
        String phone,

        String document,

        @Email
        String email,

        String street,
        String city,
        String state,
        String zipCode,
        String country,

        String notes
) {
}
