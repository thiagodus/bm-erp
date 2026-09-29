package com.bm.erp.boleto.service;

import com.bm.erp.boleto.dto.BoletoRequest;
import com.bm.erp.boleto.dto.BoletoResponse;
import com.bm.erp.boleto.dto.BoletoWebhookRequest;
import com.bm.erp.boleto.entity.Boleto;
import com.bm.erp.boleto.entity.BoletoStatus;
import com.bm.erp.boleto.exception.BoletoNotFoundException;
import com.bm.erp.boleto.repository.BoletoRepository;
import com.bm.erp.integration.boleto.client.BoletoClient;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.entity.OrderStatus;
import com.bm.erp.order.event.OrderPaidEvent;
import com.bm.erp.order.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoletoService {
    private final BoletoRepository boletoRepository;
    private final OrderService orderService;
    private final BoletoClient boletoClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BoletoService(BoletoRepository boletoRepository,
                         OrderService orderService,
                         BoletoClient boletoClient,
                         ApplicationEventPublisher applicationEventPublisher) {
        this.boletoRepository = boletoRepository;
        this.orderService = orderService;
        this.boletoClient = boletoClient;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Boleto create(UUID orderId, LocalDate dueDate){

        Optional<Boleto> foundBoleto = boletoRepository.findByOrderId(orderId);
        if(!foundBoleto.isEmpty()){
            return foundBoleto.get();
        }

        Order order = orderService.findEntityById(orderId);

        BoletoRequest boletoRequest =
                new BoletoRequest(orderId, dueDate);

        BoletoResponse boletoResponse = boletoClient.createBoleto(boletoRequest,
                orderId.toString()+":boleto");

        Boleto boleto = new Boleto();
        boleto.setOrder(order);
        boleto.setAmount(order.getTotal());
        boleto.setDueDate(dueDate);
        boleto.setExternalId(boletoResponse.externalId());
        //boleto.setStatus(BoletoStatus.valueOf(boletoResponse.status()));

        Boleto result;

        try{
            result = boletoRepository.save(boleto);
        }catch(DataIntegrityViolationException e){
            Optional<Boleto> findBoletoToSave = boletoRepository.findByOrderId(orderId);
            if(!findBoletoToSave.isEmpty()){
                result =   findBoletoToSave.get();
            }else{throw e;}
        }

        return result;
    }

    @Transactional
    public void processWebhook(BoletoWebhookRequest request){
        Boleto boleto = boletoRepository.findByExternalId(request.externalId())
                .orElseThrow(BoletoNotFoundException::new);

        BoletoStatus incomingStatus = BoletoStatus.valueOf(request.status());

        if(incomingStatus == BoletoStatus.PAID){
            boleto.markAsPaid();
            Order order = boleto.getOrder();
            order.setStatus(OrderStatus.CLOSED);

            //Publish domain event
            applicationEventPublisher.publishEvent(new OrderPaidEvent(
                    order.getId(),
                    order.getExternalId(),
                    order.getTotal()
            ));

        }else if(incomingStatus == BoletoStatus.CANCELLED){
            boleto.cancel();
        }

        boletoRepository.save(boleto);


    }
}
