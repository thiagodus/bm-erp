package com.bm.erp.integration.messaging;

import com.bm.erp.order.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderCreatedEvent event) {
        var future = kafkaTemplate.send("order-created", event.orderId().toString(), event);
        future.whenComplete((r, e) -> {
            if (e != null) {
                System.err.println("Error while sending order event to kafka");
                e.printStackTrace(System.err);
                return;
            }
            System.out.println(
                    "Kafka send succeeded: topic=" + r.getRecordMetadata().topic()
                            + ", partition=" + r.getRecordMetadata().partition()
                            + ", offset=" + r.getRecordMetadata().offset()
            );
        });
    }
}
