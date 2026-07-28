package com.bm.erp.customer.exception;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String phone) {
        super("Customer with Phone " + phone + " already exists");
    }
}
