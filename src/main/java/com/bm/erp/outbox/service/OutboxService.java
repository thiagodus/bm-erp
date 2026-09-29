package com.bm.erp.outbox.service;

import com.bm.erp.outbox.entity.OutboxEvent;
import com.bm.erp.outbox.entity.OutboxStatus;
import com.bm.erp.outbox.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;



@Service
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    public void saveEvent(String aggregateType, String aggregateId, Object event) {
        try {
            String payloadJson = objectMapper.writeValueAsString(event);
            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setAggregateType(aggregateType);
            outboxEvent.setAggregateId(aggregateId);
            outboxEvent.setEventType(event.getClass().getSimpleName());
            outboxEvent.setPayload(payloadJson);
            outboxEvent.setStatus(OutboxStatus.PENDING);

            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox event payload", e);
        }
    }
}