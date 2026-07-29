package com.bm.erp.product.dto;

import com.bm.erp.product.entity.ProductCategory;
import com.bm.erp.product.entity.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        ProductType type,
        ProductCategory category,
        BigDecimal salePrice,
        BigDecimal costPrice,
        Boolean active
) {
}
