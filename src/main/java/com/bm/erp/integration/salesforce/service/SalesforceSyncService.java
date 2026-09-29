package com.bm.erp.integration.salesforce.service;

import com.bm.erp.customer.entity.Customer;
import com.bm.erp.customer.entity.CustomerType;
import com.bm.erp.customer.repository.CustomerRepository;
import com.bm.erp.integration.salesforce.dto.SFOpportunityItems;
import com.bm.erp.integration.salesforce.dto.SFOpportunityWebhookRequest;
import com.bm.erp.order.dto.OrderResponse;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.entity.OrderItem;
import com.bm.erp.order.event.OrderCreatedEvent;
import com.bm.erp.order.mapper.OrderMapper;
import com.bm.erp.order.repository.OrderRepository;
import com.bm.erp.product.entity.Product;
import com.bm.erp.product.exception.ProductNotFoundException;
import com.bm.erp.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesforceSyncService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SalesforceSyncService(OrderRepository orderRepository,
                                 CustomerRepository customerRepository,
                                 ProductRepository productRepository,
                                 OrderMapper orderMapper,
                                 ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderResponse syncOpportunityWon(SFOpportunityWebhookRequest request){

        Optional<Order> existingOrder = orderRepository.findByExternalId(request.opportunityId());
        if(existingOrder.isPresent()){
            return orderMapper.toResponse(existingOrder.get());
        }

        Customer existingCustomer = getExistingCustomer(request);

        Map<String, Product> mappedProducts = getMappedProducts(request);

        Order order = createOrder(request, mappedProducts, existingCustomer);

        Order savedOrder = orderRepository.save(order);

        publishOrder(savedOrder, existingCustomer);

        return orderMapper.toResponse(savedOrder);

    }

    private void publishOrder(Order savedOrder, Customer existingCustomer) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                savedOrder.getId(),
                existingCustomer.getId(),
                existingCustomer.getName(),
                savedOrder.getTotal()
        );

        applicationEventPublisher.publishEvent(orderCreatedEvent);
    }

    private static @NonNull Order createOrder(SFOpportunityWebhookRequest request, Map<String, Product> mappedProducts, Customer customer) {
        Order order = new Order();
        order.setExternalId(request.opportunityId());
        order.setNotes("Opportunity Won in Salesforce");
        order.setCustomer(customer);

        for(SFOpportunityItems item : request.items()){
            Product product = mappedProducts.get(item.sku());
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());
            orderItem.setUnitPrice(item.unitPrice());
            orderItem.setNotes("Salesforce synced item");
            order.addItem(orderItem);
        }

        order.updateTotal();
        return order;
    }

    private @NonNull Map<String, Product> getMappedProducts(SFOpportunityWebhookRequest request) {
        Set<String> skus = request.items().stream()
                .map(SFOpportunityItems::sku)
                .collect(Collectors.toSet());

        Map<String, Product> mappedProducts = productRepository.findBySkuIn(skus).stream()
                .collect(Collectors.toMap(Product::getSku, Function.identity()));

        Set<String> missingSkus = skus.stream().
                filter(sku -> !mappedProducts.containsKey(sku))
                .collect(Collectors.toSet());

        if(!missingSkus.isEmpty()) {
            String message = "The following skus are missing: ";
            for(String sku : missingSkus) {
                message += sku + " ";
            }
            throw new ProductNotFoundException(message);
        }
        return mappedProducts;
    }

    private @NonNull Customer getExistingCustomer(SFOpportunityWebhookRequest request) {
        return customerRepository.findByDocument(request.accountTaxId())
                .orElseGet(() -> {
                    Customer customer = new Customer();
                    customer.setDocument(request.accountTaxId());
                    customer.setName(request.accountName());
                    customer.setType(request.accountTaxId().length() > 11 ? CustomerType.COMPANY : CustomerType.INDIVIDUAL);
                    customer.setPhone("N/A - SF");
                    return customerRepository.save(customer);
                });
    }
}
