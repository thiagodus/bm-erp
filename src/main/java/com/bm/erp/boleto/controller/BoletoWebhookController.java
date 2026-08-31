package com.bm.erp.boleto.controller;

import com.bm.erp.boleto.dto.BoletoWebhookRequest;
import com.bm.erp.boleto.service.BoletoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/boletos")
public class BoletoWebhookController {

    private final BoletoService boletoService;

    public BoletoWebhookController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @PostMapping
    public ResponseEntity<Void> receiveWebhook(@RequestBody BoletoWebhookRequest request){
        boletoService.processWebhook(request);
        return ResponseEntity.ok().build();
    }


}
