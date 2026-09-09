package com.bm.erp.integration.messaging;

import com.bm.erp.order.event.OrderCreatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventListener {
    private final OrderEventProducer orderEventProducer;

    public OrderEventListener(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        orderEventProducer.publish(orderCreatedEvent);
    }
}
