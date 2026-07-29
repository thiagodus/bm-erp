package com.bm.erp.product.service;

import com.bm.erp.product.dto.ProductRequest;
import com.bm.erp.product.dto.ProductResponse;
import com.bm.erp.product.entity.Product;
import com.bm.erp.product.entity.ProductCategory;
import com.bm.erp.product.entity.ProductType;
import com.bm.erp.product.exception.ProductNotFoundException;
import com.bm.erp.product.mapper.ProductMapper;
import com.bm.erp.product.repository.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProduct() {
        //Arrange
        Product product = new Product();

        ProductRequest productRequest = getProductRequest();

        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        ProductResponse productResponse = getProductResponse();

        when(productMapper.toResponse(product)).thenReturn(productResponse);

        //Act
        ProductResponse savedResponse = productService.save(productRequest);

        //Assert
        assertThat(savedResponse).isEqualTo(productResponse);
        verify(productRepository).save(product);
        verify(productMapper).toResponse(product);
        verify(productMapper).toEntity(productRequest);

    }

    @Test
    void shouldReturnProductWhenIdExists() {
        //Arrange
        UUID uuid = UUID.randomUUID();
        Product  product = new Product();
        ProductResponse productResponse = getProductResponse();

        when(productMapper.toResponse(product)).thenReturn(productResponse);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));

        //Act
        ProductResponse foundResponse = productService.getById(uuid);

        //Assert
        assertThat(foundResponse).isEqualTo(productResponse);

        verify(productRepository).findById(uuid);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        //Arrange
        UUID uuid = UUID.randomUUID();
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> productService.getById(uuid)).isInstanceOf(ProductNotFoundException.class);
        verify(productRepository).findById(uuid);
    }

    @Test
    void shouldReturnAllProducts(){
        //Arrange
        Product product1 = new Product();
        Product product2 = new Product();

        ProductResponse productResponse1 = getProductResponse();
        ProductResponse productResponse2 = getProductResponse();

        when(productMapper.toResponse(product1)).thenReturn(productResponse1);
        when(productMapper.toResponse(product2)).thenReturn(productResponse2);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        //Act

        List<ProductResponse> allProducts = productService.getAll();

        //Assert
        assertThat(allProducts).containsExactly(productResponse1, productResponse2);
        verify(productRepository).findAll();
        verify(productMapper).toResponse(product1);
        verify(productMapper).toResponse(product2);

    }

    private static @NonNull ProductResponse getProductResponse() {
        ProductResponse productResponse = new ProductResponse(
                UUID.randomUUID(),
                "Product 1",
                "Description 1",
                ProductType.PRODUCT,
                ProductCategory.CLOTHING,
                BigDecimal.valueOf(50),
                null,
                true

        );
        return productResponse;
    }

    private static @NonNull ProductRequest getProductRequest() {
        ProductRequest productRequest = new ProductRequest(
                "Product 1",
                "Description 1",
                ProductType.PRODUCT,
                ProductCategory.CLOTHING,
                BigDecimal.valueOf(50),
                null

        );
        return productRequest;
    }
}
