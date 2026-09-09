package com.bm.erp.boleto.exception;

public class BoletoNotFoundException extends RuntimeException{
    public BoletoNotFoundException() { super("No Boleto found"); }
}
