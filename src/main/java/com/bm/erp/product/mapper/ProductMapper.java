package com.bm.erp.product.mapper;

import com.bm.erp.product.dto.ProductRequest;
import com.bm.erp.product.dto.ProductResponse;
import com.bm.erp.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductRequest request) {
        Product product = new Product();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setType(request.type());
        product.setCategory(request.category());
        product.setSalePrice(request.salePrice());
        product.setCostPrice(request.costPrice());

        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getType(),
                product.getCategory(),
                product.getSalePrice(),
                product.getCostPrice(),
                product.getActive()
        );
    }
}
