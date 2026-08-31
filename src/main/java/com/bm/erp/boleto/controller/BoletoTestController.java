package com.bm.erp.boleto.controller;

import com.bm.erp.boleto.dto.BoletoRequest;
import com.bm.erp.boleto.dto.BoletoResponse;
import com.bm.erp.integration.boleto.client.BoletoClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class BoletoTestController {
    private BoletoClient boletoClient;
    public BoletoTestController(BoletoClient boletoClient) {
        this.boletoClient = boletoClient;
    }

    @PostMapping("/boleto")
    public BoletoResponse create(@RequestBody BoletoRequest boletoRequest, @RequestHeader("Idempotency-Key") String idempotencyKey){
        return boletoClient.createBoleto(boletoRequest, idempotencyKey);
        
    }
}
