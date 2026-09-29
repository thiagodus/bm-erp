package com.bm.erp.product.repository;

import com.bm.erp.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findBySkuIn(Collection<String> sku);
}

