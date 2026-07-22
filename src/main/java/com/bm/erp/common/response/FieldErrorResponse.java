package com.bm.erp.common.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}
