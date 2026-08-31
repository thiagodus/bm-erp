package com.bm.erp.integration.nfe;

import com.bm.erp.integration.nfe.dto.NfeResponse;
import com.bm.erp.order.entity.Order;

public interface NfeProvider {
    NfeResponse issueInvoice(Order order);
}
