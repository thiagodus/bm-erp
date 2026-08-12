package com.bm.erp.order.mapper;

import com.bm.erp.customer.entity.Customer;
import com.bm.erp.order.dto.OrderItemRequest;
import com.bm.erp.order.dto.OrderItemResponse;
import com.bm.erp.order.dto.OrderRequest;
import com.bm.erp.order.dto.OrderResponse;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.entity.OrderItem;
import com.bm.erp.product.entity.Product;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMapper {
    public Order toEntity(OrderRequest orderRequest, Customer customer) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setNotes(orderRequest.notes());

        return order;
    }

    public OrderItem toEntity(OrderItemRequest orderItemRequest, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequest.quantity());
        orderItem.setUnitPrice(orderItemRequest.unitPrice());
        orderItem.setNotes(orderItemRequest.notes());

        return orderItem;
    }

    public OrderResponse toResponse(Order order) {

        UUID customerId = order.getCustomer() == null ? null : order.getCustomer().getId();
        String customerName = order.getCustomer() == null ? null : order.getCustomer().getName();
        return new OrderResponse(
                order.getId(),
                customerId,
                customerName,
                order.getStatus(),
                order.getNotes(),
                order.getTotal(),
                order.getItems().stream().map(this::toResponse).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );



    }

    public OrderItemResponse toResponse(OrderItem orderItem) {
       return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getNotes()
        );


    }
}
