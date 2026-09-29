package com.bm.erp.order.service;

import com.bm.erp.customer.entity.Customer;
import com.bm.erp.customer.exception.CustomerNotFoundException;
import com.bm.erp.customer.repository.CustomerRepository;
import com.bm.erp.order.dto.OrderItemRequest;
import com.bm.erp.order.dto.OrderItemResponse;
import com.bm.erp.order.dto.OrderRequest;
import com.bm.erp.order.dto.OrderResponse;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.entity.OrderItem;
import com.bm.erp.order.entity.OrderStatus;
import com.bm.erp.order.event.OrderCreatedEvent;
import com.bm.erp.order.mapper.OrderMapper;
import com.bm.erp.order.repository.OrderRepository;
import com.bm.erp.outbox.service.OutboxService;
import com.bm.erp.product.entity.Product;
import com.bm.erp.product.exception.ProductNotFoundException;
import com.bm.erp.product.repository.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private final UUID customerId = UUID.randomUUID();
    private final UUID productId1 = UUID.randomUUID();
    private final UUID productId2 = UUID.randomUUID();
    private  Product product1 = new Product();
    private  Product product2 = new Product();
    private  Customer customer = new Customer();

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    //@Mock
    //private OrderEventProducer orderEventProducer;

    @Mock
    private OutboxService  outboxService;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest getOrderRequest(UUID id) {
        return new OrderRequest(
                id,
                "Some notes for the order",
                List.of(getOrderItemRequest1(), getOrderItemRequest2())

        );
     }

    private Order getOrder(boolean anonymous) {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        if(!anonymous){
            order.setCustomer(customer);
        }
        order.setNotes("Some notes for the order");

        return order;
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


    private  @NonNull OrderItem getOrderItem1() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product1);
        orderItem.setQuantity(1);
        orderItem.setUnitPrice(BigDecimal.TEN);
        orderItem.setNotes("Some notes for this item 1");

        return orderItem;
    }

    private  @NonNull OrderItem getOrderItem2() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product2);
        orderItem.setQuantity(2);
        orderItem.setUnitPrice(BigDecimal.valueOf(1.5));
        orderItem.setNotes("Some notes for this item 2");

        return orderItem;
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



    @Test
    public void shouldCreateOrder() {
        //Arrange

        customer.setId(customerId);
        customer.setName("Customer");

        OrderRequest orderRequest = getOrderRequest(customerId);
        OrderItemRequest item1 = orderRequest.items().get(0);
        OrderItemRequest item2 = orderRequest.items().get(1);
        Order order =  getOrder(false);
        OrderItem orderItem1 = getOrderItem1();
        OrderItem orderItem2 = getOrderItem2();
        OrderResponse orderResponse = getOrderResponse(
                getOrderItemResponse1(), getOrderItemResponse2());
        Set<UUID> productIds = new HashSet<>();
        product1.setId(productId1);
        product2.setId(productId2);
        productIds.add(productId1);
        productIds.add(productId2);

        when(customerRepository.findById(orderRequest.customerId()))
                .thenReturn(Optional.of(customer));
        when(orderMapper.toEntity(orderRequest, customer)).thenReturn(order);
        when(productRepository.findAllById(productIds)).thenReturn(List.of(product1, product2));
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);
        when (orderMapper.toEntity(item1, product1)).thenReturn(orderItem1);
        when (orderMapper.toEntity(item2, product2)).thenReturn(orderItem2);
        when(orderRepository.save(order)).thenReturn(order);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

        //Act
        OrderResponse savedOrder = orderService.save(orderRequest);
        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

        //Assert
        verify(outboxService).saveEvent(eq("ORDER"), eq(order.getId().toString()), eventCaptor.capture());
       //h(eventCaptor.capture());
        OrderCreatedEvent orderCreatedEvent = eventCaptor.getValue();
        assertThat(orderCreatedEvent.orderId()).isEqualTo(order.getId());
        assertThat(orderCreatedEvent.customerId()).isEqualTo(customerId);
        assertThat(orderCreatedEvent.customerName()).isEqualTo("Customer");
        assertThat(orderCreatedEvent.total()).isEqualByComparingTo(new BigDecimal("13.00"));

        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order orderCaptured = orderArgumentCaptor.getValue();

        assertThat(orderCaptured.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(13));
        assertThat(orderCaptured.getItems()).hasSize(2);
        assertThat(orderCaptured.getItems()).allMatch(item -> item.getOrder() == orderCaptured);
        assertThat(orderCaptured.getCustomer()).isEqualTo(customer);
        assertThat(savedOrder).isEqualTo(orderResponse);

        verify(orderMapper).toEntity(orderRequest, customer);
        verify(orderMapper).toEntity(getOrderItemRequest1(), product1);
        verify(orderMapper).toEntity(getOrderItemRequest2(), product2);
        verify(orderMapper).toResponse(order);
        verify(customerRepository).findById(customerId);


    }

    @Test
    void shouldThrowProductNotFound(){
        //Arrange
        OrderRequest orderRequest = getOrderRequest(customerId);
        OrderItemRequest item1 = orderRequest.items().get(0);
        OrderItemRequest item2 = orderRequest.items().get(1);
        Order order =  getOrder(false);
        OrderItem orderItem1 = getOrderItem1();
        OrderItem orderItem2 = getOrderItem2();

        when(customerRepository.findById(orderRequest.customerId()))
                .thenReturn(Optional.of(customer));
        when(orderMapper.toEntity(orderRequest, customer)).thenReturn(order);
        when(productRepository.findById(productId1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(productId2)).thenReturn(Optional.empty());
        when (orderMapper.toEntity(item1, product1)).thenReturn(orderItem1);

        //Act //Assert
        assertThatThrownBy(() -> orderService.save(orderRequest))
            .isInstanceOf(ProductNotFoundException.class);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldThrowCustomerNotFound(){
        //Arrange
        OrderRequest orderRequest = getOrderRequest(customerId);
        OrderItemRequest item1 = orderRequest.items().get(0);
        OrderItemRequest item2 = orderRequest.items().get(1);
        Order order =  getOrder(false);
        OrderItem orderItem1 = getOrderItem1();
        OrderItem orderItem2 = getOrderItem2();

        when(customerRepository.findById(orderRequest.customerId()))
                .thenReturn(Optional.empty());

        //Act //Assert
        assertThatThrownBy(() -> orderService.save(orderRequest))
                .isInstanceOf(CustomerNotFoundException.class);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldCreateAnonymousOrder(){
        //Arrange
        OrderRequest orderRequest = getOrderRequest(null);
        OrderItemRequest item1 = orderRequest.items().get(0);
        OrderItemRequest item2 = orderRequest.items().get(1);
        Order order =  getOrder(true);
        OrderItem orderItem1 = getOrderItem1();
        OrderItem orderItem2 = getOrderItem2();
        when(orderMapper.toEntity(orderRequest, null)).thenReturn(order);
        when(productRepository.findById(productId1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(productId2)).thenReturn(Optional.of(product2));
        when (orderMapper.toEntity(item1, product1)).thenReturn(orderItem1);
        when (orderMapper.toEntity(item2, product2)).thenReturn(orderItem2);
        when(orderRepository.save(order)).thenReturn(order);

        //Act
        OrderResponse saved = orderService.save(orderRequest);
        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        //verify(orderEventProducer).publish(eventCaptor.capture());
        OrderCreatedEvent orderCreatedEvent = eventCaptor.getValue();

        //Assert
        assertThat(orderCreatedEvent.customerId()).isNull();
        assertThat(orderRepository.save(order));
        verifyNoInteractions(customerRepository);
    }

}
