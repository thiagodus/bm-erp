package com.bm.erp.outbox.scheduler;

import com.bm.erp.config.RabbitConfig;
import com.bm.erp.outbox.entity.OutboxEvent;
import com.bm.erp.outbox.entity.OutboxStatus;
import com.bm.erp.outbox.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxPublisher(OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // Runs every 5 seconds
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Found {} pending outbox event(s) to publish.", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                // Publish JSON payload to RabbitMQ
                rabbitTemplate.convertAndSend(
                        RabbitConfig.EXCHANGE_NAME,
                        RabbitConfig.ORDER_CREATED_ROUTING_KEY,
                        event.getPayload()
                );

                // Mark event as PROCESSED in PostgreSQL
                event.setStatus(OutboxStatus.SENT);
                event.setProcessedAt(Instant.now());
                outboxRepository.save(event);

                log.info("Successfully published outbox event: {} [{}]", event.getId(), event.getEventType());
            } catch (Exception ex) {
                log.error("Failed to publish outbox event: {}", event.getId(), ex);
                // Keep PENDING to retry on next cycle (At-Least-Once Delivery)
            }
        }
    }
}