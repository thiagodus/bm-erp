package com.bm.erp.order.controller;

import com.bm.erp.order.dto.OrderItemRequest;
import com.bm.erp.order.dto.OrderItemResponse;
import com.bm.erp.order.dto.OrderRequest;
import com.bm.erp.order.dto.OrderResponse;
import com.bm.erp.order.entity.OrderStatus;
import com.bm.erp.order.service.OrderService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    private final UUID customerId = UUID.randomUUID();
    private final UUID productId1 = UUID.randomUUID();
    private final UUID productId2 = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldCreateOrder() throws Exception {
        //Arrange
        OrderRequest orderRequest = getOrderRequest(null);
        OrderResponse orderResponse = getOrderResponse(
                getOrderItemResponse1(), getOrderItemResponse2());

        when(orderService.save(orderRequest)).thenReturn(orderResponse);
        //Act
        mockMvc.perform(
                        post("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequest))
                )
                .andExpect(status().isCreated());
        //Assert
        verify(orderService).save(orderRequest);
    }

    private OrderRequest getOrderRequest(UUID id) {
        return new OrderRequest(
                id,
                "Some notes for the order",
                List.of(getOrderItemRequest1(), getOrderItemRequest2())

        );
    }
    private @NonNull OrderItemRequest getOrderItemRequest1() {
        OrderItemRequest orderItemRequest1 = new OrderItemRequest(
                productId1,
                1,
                BigDecimal.TEN,
                "Some notes for this item 1"
        );
        return orderItemRequest1;
    }

    private  @NonNull OrderItemRequest getOrderItemRequest2() {
        OrderItemRequest orderItemRequest2 = new OrderItemRequest(
                productId2,
                2,
                BigDecimal.valueOf(1.5),
                "Some notes for this item 2"
        );
        return orderItemRequest2;
    }
    private @NonNull OrderItemResponse getOrderItemResponse2() {
        return new OrderItemResponse(UUID.randomUUID(),
                productId2, "product 2", 2, BigDecimal.valueOf(1.5), "Some notes for this item 2");
    }

    private @NonNull OrderItemResponse getOrderItemResponse1() {
        return new OrderItemResponse(UUID.randomUUID(),
                productId1, "product 1", 1, BigDecimal.TEN, "Some notes for this item 1");
    }

    private @NonNull OrderResponse getOrderResponse(OrderItemResponse orderItemResponse1, OrderItemResponse orderItemResponse2) {
        return new OrderResponse(UUID.randomUUID(),
                customerId,
                "Name",
                OrderStatus.OPEN,
                "Note 1",
                BigDecimal.valueOf(13),
                List.of(orderItemResponse1, orderItemResponse2),
                Instant.now(),
                Instant.now());
    }
}
