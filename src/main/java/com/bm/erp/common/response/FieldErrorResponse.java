package com.bm.erp.common.response;

public record FieldErrorResponse(
        String field,
        String message
) {
}
