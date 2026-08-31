package com.bm.erp.boleto.dto;

import com.bm.erp.boleto.entity.BoletoStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BoletoResponse(UUID orderId,
                             String externalId,
                             BigDecimal amount,
                             LocalDate dueDate,
                             String status) {
}
