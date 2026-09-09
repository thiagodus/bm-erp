package com.bm.erp.integration.messaging;

import com.bm.erp.order.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {
    @KafkaListener(topics = "order-created", groupId = "erp-debug")
    public void consume(OrderCreatedEvent event) {
        System.out.println(
                "Received OrderCreatedEvent: " + event
        );
    }
}
