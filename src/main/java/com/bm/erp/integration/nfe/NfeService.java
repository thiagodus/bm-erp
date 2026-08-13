package com.bm.erp.integration.nfe;

import com.bm.erp.integration.nfe.dto.NfeRequest;
import com.bm.erp.integration.nfe.dto.NfeResponse;
import com.bm.erp.order.entity.Order;
import org.springframework.stereotype.Service;

@Service
public class NfeService {
    private NfeClient nfeClient;
    public NfeService(NfeClient nfeClient) {
        this.nfeClient = nfeClient;
    }
    public NfeResponse issueInvoice(Order order) {
        NfeRequest request = new NfeRequest(
                order.getCustomer().getName(),
                "Order " + order.getId(),
                order.getTotal());
        return nfeClient.issueInvoice(request);
    }
}
