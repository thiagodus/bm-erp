package com.bm.erp.integration.nfe.service;

import com.bm.erp.integration.nfe.NfeProvider;
import com.bm.erp.integration.nfe.dto.NfeResponse;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.exception.OrderNotFoundException;
import com.bm.erp.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NfeService {

    private final NfeProvider provider;
    private final OrderRepository orderRepository;

    public NfeService(NfeProvider provider,  OrderRepository orderRepository) {
        this.provider = provider;
        this.orderRepository = orderRepository;
    }

    public NfeResponse issueInvoice(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        return provider.issueInvoice(order);
    }
}
