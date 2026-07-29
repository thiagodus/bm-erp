package com.bm.erp.product.service;

import com.bm.erp.product.dto.ProductRequest;
import com.bm.erp.product.dto.ProductResponse;
import com.bm.erp.product.entity.Product;
import com.bm.erp.product.exception.ProductNotFoundException;
import com.bm.erp.product.mapper.ProductMapper;
import com.bm.erp.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse save(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct =  productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public ProductResponse getById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.toResponse(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }
}
