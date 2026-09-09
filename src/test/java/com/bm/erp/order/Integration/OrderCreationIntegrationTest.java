package com.bm.erp.order.Integration;

import com.bm.erp.integration.messaging.OrderEventProducer;
import com.bm.erp.order.dto.OrderItemRequest;
import com.bm.erp.order.dto.OrderRequest;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.event.OrderCreatedEvent;
import com.bm.erp.order.repository.OrderRepository;
import com.bm.erp.product.entity.Product;
import com.bm.erp.product.entity.ProductCategory;
import com.bm.erp.product.entity.ProductType;
import com.bm.erp.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest(properties = "spring.kafka.listener.auto-startup=false")
@AutoConfigureMockMvc
public class OrderCreationIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"));

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderEventProducer orderEventProducer;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void createsOrderAndPublishesEvent() throws Exception {

        Product product = new Product();
        product.setName("Product 1");
        product.setType(ProductType.PRODUCT);
        product.setCategory(ProductCategory.OTHER);
        product.setSalePrice(BigDecimal.TEN);
        product.setActive(true);
        Product savedProduct = productRepository.save(product);

        String note = "test-order-" + UUID.randomUUID();

        OrderRequest orderRequest = new OrderRequest(
                null, note, List.of(
                        new OrderItemRequest(savedProduct.getId(), 2, BigDecimal.valueOf(15), "Test Product item")
                )
        );

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated());

        Order persistedOrder = orderRepository.findAll()
                .stream()
                .filter(o -> note.equals(o.getNotes()))
                .findFirst()
                .orElseThrow();

        assertThat(persistedOrder.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(30.00));

        ArgumentCaptor<OrderCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(OrderCreatedEvent.class);

        verify(orderEventProducer).publish(eventCaptor.capture());

        OrderCreatedEvent publishedEvent = eventCaptor.getValue();

        assertThat(publishedEvent.orderId()).isEqualTo(persistedOrder.getId());



    }
}
