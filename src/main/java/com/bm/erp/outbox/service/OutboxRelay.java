package com.bm.erp.outbox.service;


import com.bm.erp.outbox.entity.OutboxEvent;
import com.bm.erp.outbox.entity.OutboxStatus;
import com.bm.erp.outbox.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxRelay {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    public OutboxRelay(OutboxRepository outboxRepository,  KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${outbox.poll-interval-ms:2000}")
    @Transactional
    public void processOutboxEvents(){
        List<OutboxEvent> outboxEvents = outboxRepository.findPendingEventsForProcessing(PageRequest.of(0, 100));

        if(outboxEvents.isEmpty()){return;}

            for(OutboxEvent event : outboxEvents) {
                try {
                    String topic = resolveTopic(event);
                    String partitionKey = event.getAggregateId();
                    String payload = event.getPayload();

                    kafkaTemplate.send(topic, partitionKey, payload).get();

                    event.setStatus(OutboxStatus.SENT);
                    event.setProcessedAt(Instant.now());


                }catch(Exception e){
                    log.error("Failed to publish outbox event id: {}", event.getId(), e);
                    event.setStatus(OutboxStatus.FAILED);
                }
            }

    }

    private String resolveTopic(OutboxEvent event) {
        return switch (event.getEventType()){
            case "OrderCreatedEvent" -> "order-created";
            case "OrderPaidEvent" -> "order-paid";
            default -> "erp-" + event.getAggregateType().toLowerCase() + "-events";
        };
    }
}
