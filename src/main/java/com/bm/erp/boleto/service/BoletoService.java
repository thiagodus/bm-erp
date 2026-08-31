package com.bm.erp.boleto.service;

import com.bm.erp.boleto.dto.BoletoRequest;
import com.bm.erp.boleto.dto.BoletoResponse;
import com.bm.erp.boleto.dto.BoletoWebhookRequest;
import com.bm.erp.boleto.entity.Boleto;
import com.bm.erp.boleto.entity.BoletoStatus;
import com.bm.erp.boleto.repository.BoletoRepository;
import com.bm.erp.integration.boleto.client.BoletoClient;
import com.bm.erp.order.entity.Order;
import com.bm.erp.order.service.OrderService;
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

    public BoletoService(BoletoRepository boletoRepository, OrderService orderService,  BoletoClient boletoClient) {
        this.boletoRepository = boletoRepository;
        this.orderService = orderService;
        this.boletoClient = boletoClient;
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
        boleto.setStatus(BoletoStatus.valueOf(boletoResponse.status()));

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

    public void processWebhook(BoletoWebhookRequest request){
        Optional<Boleto> boleto = boletoRepository.findByExternalId(request.externalId());
        if(boleto.isEmpty()){
           //log
            return;
        }
        Boleto boletoFound = boleto.get();
        boletoFound.setStatus(BoletoStatus.valueOf(request.status()));
        boletoRepository.save(boletoFound);


    }
}
