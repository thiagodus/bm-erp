package com.bm.erp.order.Integration;

import com.bm.erp.order.dto.OrderItemRequest;
import com.bm.erp.order.dto.OrderRequest;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.repository.OrderRepository;
import com.bm.erp.product.entity.Product;
import com.bm.erp.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldCreateOrder() throws Exception {
        // Arrange
        Product product = productRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        OrderRequest request = new OrderRequest(
                null,
                "Integration test order",
                List.of(
                        new OrderItemRequest(
                                product.getId(),
                                2,
                                new BigDecimal("15.00"),
                                "Integration test item"
                        )
                )
        );

        // Act
        mockMvc.perform(
                        post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        List<Order> orders = orderRepository.findAll();

        assertThat(orders)
                .anyMatch(order ->
                        "Integration test order".equals(order.getNotes()));
    }
}
