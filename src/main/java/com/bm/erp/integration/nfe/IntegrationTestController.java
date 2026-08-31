package com.bm.erp.integration.nfe;

import com.bm.erp.integration.nfe.dto.NfeResponse;
import com.bm.erp.integration.nfe.service.NfeService;
import com.bm.erp.order.service.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
 class IntegrationTestController {
    private final NfeService nfeService;
    private final OrderService orderService;
    public IntegrationTestController(NfeService nfeService,  OrderService orderService) {
        this.nfeService = nfeService;
        this.orderService = orderService;
    }


    @PostMapping("/nfe/{orderId}")
    public NfeResponse issueInvoice(@PathVariable UUID orderId) {
         return nfeService.issueInvoice(orderId);
    }
}
