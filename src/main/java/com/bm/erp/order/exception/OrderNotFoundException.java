package com.bm.erp.order.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(){super("No Order found");}
}
