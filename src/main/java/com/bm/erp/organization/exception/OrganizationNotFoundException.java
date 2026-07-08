package com.bm.erp.organization.exception;

public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException() {
        super("No organization found");
    }
}
