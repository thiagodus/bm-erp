package com.bm.erp.customer.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("No Customer found");
    }
}
