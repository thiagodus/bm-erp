package com.bm.erp.integration.boleto;

import com.bm.erp.boleto.dto.BoletoRequest;
import com.bm.erp.integration.boleto.dto.FBoletoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-provider/boletos")
public class FakeBoletoProviderController {

    private final FakeBoletoProvider provider;

    public FakeBoletoProviderController(FakeBoletoProvider provider) {
        this.provider = provider;
    }

    @PostMapping
    public FBoletoResponse create(@RequestBody BoletoRequest boletoRequest, @RequestHeader("Idempotency-Key") String idempotencyKey) {

        return provider.createBoleto(idempotencyKey, boletoRequest);

    }

    @PostMapping("/pay")
    public ResponseEntity<?> webhookUpdateStatus(@RequestParam String externalId){
        provider.simulatePayment(externalId, "PAID");
        return ResponseEntity.ok().build();
    }

}
