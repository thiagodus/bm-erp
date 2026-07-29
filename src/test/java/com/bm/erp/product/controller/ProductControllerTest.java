package com.bm.erp.product.controller;

import com.bm.erp.config.SecurityConfig;
import com.bm.erp.product.dto.ProductResponse;
import com.bm.erp.product.entity.ProductCategory;
import com.bm.erp.product.entity.ProductType;
import com.bm.erp.product.service.ProductService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

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

    @Test
    void shouldReturnProductById() throws Exception {
        //Arrange
        ProductResponse productResponse = getProductResponse();
        UUID id = UUID.randomUUID();
        when(productService.getById(id)).thenReturn(productResponse);
        //Act //Assert
        mockMvc.perform(get("/product/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value(productResponse.name()))
                .andExpect(jsonPath("$.type")
                        .value(productResponse.type().name()))
                .andExpect(jsonPath("$.category")
                        .value(productResponse.category().name()));

        verify(productService).getById(id);
    }
}
