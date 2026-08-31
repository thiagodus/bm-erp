package com.bm.erp.boleto.controller;

import com.bm.erp.boleto.dto.BoletoRequest;
import com.bm.erp.boleto.dto.BoletoResponse;
import com.bm.erp.boleto.entity.Boleto;
import com.bm.erp.boleto.service.BoletoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boletos")
public class BoletoController {
    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @PostMapping
    public BoletoResponse createBoleto(@RequestBody BoletoRequest boletoRequest) {
        Boleto boleto = boletoService.create(boletoRequest.orderId(), boletoRequest.dueDate());
        return new BoletoResponse(
                boleto.getOrder().getId(),
                boleto.getExternalId(),
                boleto.getAmount(),
                boleto.getDueDate(),
                boleto.getStatus().toString()
        );
    }
}
