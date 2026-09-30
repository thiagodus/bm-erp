package com.bm.erp.order.listener;

import com.bm.erp.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @RabbitListener(queues = RabbitConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(String payload) {
        log.info("🔥 [RabbitMQ Consumer] Received OrderCreated event: {}", payload);

        // Downstream processing: (e.g., generate Boleto, notify customer, trigger billing)
        log.info("📦 [Downstream Action] Initiating automatic Boleto/Invoice generation for received order...");
    }
}