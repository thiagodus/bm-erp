package com.bm.erp.boleto.dto;

import java.time.LocalDate;
import java.util.UUID;

public record BoletoRequest(
        UUID orderId,
        LocalDate dueDate
) {
}
