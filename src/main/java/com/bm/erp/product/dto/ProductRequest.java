package com.bm.erp.product.dto;

import com.bm.erp.product.entity.ProductCategory;
import com.bm.erp.product.entity.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank
        String name,

        String description,

        @NotNull
        ProductType type,

        @NotNull
        ProductCategory category,

        @NotNull
        @DecimalMin(value = "0.00")
        BigDecimal salePrice,

        @DecimalMin(value = "0.00")
        BigDecimal costPrice
) {
}
