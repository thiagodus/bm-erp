package com.bm.erp.integration.nfe.exception;

public class NfeIntegrationException extends RuntimeException{
    public NfeIntegrationException(String message){
        super(message);
    }
    public NfeIntegrationException(String message, Throwable cause){
        super(message,cause);
    }
}
