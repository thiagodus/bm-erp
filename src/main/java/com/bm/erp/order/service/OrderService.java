package com.bm.erp.order.service;

import com.bm.erp.customer.entity.Customer;
import com.bm.erp.customer.exception.CustomerNotFoundException;
import com.bm.erp.customer.repository.CustomerRepository;
import com.bm.erp.order.dto.OrderItemRequest;
import com.bm.erp.order.dto.OrderRequest;
import com.bm.erp.order.dto.OrderResponse;
import com.bm.erp.order.dto.OrderUpdateRequest;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.entity.OrderItem;
import com.bm.erp.order.entity.OrderStatus;
import com.bm.erp.order.event.OrderCreatedEvent;
import com.bm.erp.order.exception.OrderNotEditableException;
import com.bm.erp.order.exception.OrderNotFoundException;
import com.bm.erp.order.mapper.OrderMapper;
import com.bm.erp.order.repository.OrderRepository;
import com.bm.erp.outbox.service.OutboxService;
import com.bm.erp.product.entity.Product;
import com.bm.erp.product.exception.ProductNotFoundException;
import com.bm.erp.product.repository.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    //private final OrderEventProducer orderEventProducer;
    //private final ApplicationEventPublisher applicationEventPublisher;
    private final OutboxService outboxService;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, OrderMapper orderMapper,  OutboxService outboxService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.outboxService = outboxService;

    }


    @Transactional
    public OrderResponse save(OrderRequest orderRequest) {
        Customer customer = null;

        if(orderRequest.customerId() != null){
            customer = customerRepository.findById(orderRequest.customerId()).orElseThrow(CustomerNotFoundException::new);
        }

        Order order = orderMapper.toEntity(orderRequest, customer);

        Map<UUID, Product> orderProductMap = fetchProductsMap(orderRequest.items());

        for(OrderItemRequest itemRequest : orderRequest.items()){
            Product product = orderProductMap.get(itemRequest.productId());
            if(product == null){
                throw new ProductNotFoundException();
            }
            OrderItem orderItem = orderMapper.toEntity(itemRequest, product);
            order.addItem(orderItem);
        }

        order.updateTotal();

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                savedOrder.getId(),
                customer == null ? null : savedOrder.getCustomer().getId(),
                customer == null ? null : savedOrder.getCustomer().getName(),
                savedOrder.getTotal()
        );

        //applicationEventPublisher.publishEvent(orderCreatedEvent);
        outboxService.saveEvent("ORDER", order.getId().toString(), orderCreatedEvent);

        return  orderMapper.toResponse(savedOrder);


    }



    public OrderResponse findById(UUID orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return  orderMapper.toResponse(order);
    }

    public List<OrderResponse> findAll(){
        List<OrderResponse> response = orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
        return response;
    }

    @Transactional
    public OrderResponse update(UUID orderId, OrderUpdateRequest request){
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(order.getStatus() != OrderStatus.OPEN){
            throw new OrderNotEditableException();
        }

        Customer customer = null;
        if(request.customerId() != null){
            customer = customerRepository.findById(request.customerId()).orElseThrow(CustomerNotFoundException::new);
        }

        order.setCustomer(customer);
        order.setNotes(request.notes());

        order.getItems().clear();

        Map<UUID, Product> orderProductMap = fetchProductsMap(request.items());

        for(OrderItemRequest itemRequest : request.items()){
            Product product = orderProductMap.get(itemRequest.productId());
            if(product == null){
                throw new ProductNotFoundException();
            }
            OrderItem orderItem = orderMapper.toEntity(itemRequest, product);
            order.addItem(orderItem);
        }

        order.updateTotal();

        Order saved = orderRepository.save(order);

        return  orderMapper.toResponse(saved);

    }

    public Order findEntityById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException());
    }

    private @NonNull Map<UUID, Product> fetchProductsMap(List<OrderItemRequest> items) {
        Set<UUID> productIds = items
                .stream()
                .map(OrderItemRequest::productId)
                .collect(Collectors.toSet());

        Map<UUID, Product> orderProductMap = productRepository
                .findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        return orderProductMap;
    }
}
