package com.bm.erp.customer.mapper;

import com.bm.erp.customer.dto.CustomerRequest;
import com.bm.erp.customer.dto.CustomerResponse;
import com.bm.erp.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();

        customer.setType(request.type());
        customer.setName(request.name());
        customer.setPhone(request.phone());
        customer.setDocument(request.document());
        customer.setEmail(request.email());
        customer.setStreet(request.street());
        customer.setCity(request.city());
        customer.setState(request.state());
        customer.setZipCode(request.zipCode());
        customer.setCountry(request.country());
        customer.setNotes(request.notes());

        return customer;
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getType(),
                customer.getName(),
                customer.getPhone(),
                customer.getDocument(),
                customer.getEmail(),
                customer.getStreet(),
                customer.getCity(),
                customer.getState(),
                customer.getZipCode(),
                customer.getCountry(),
                customer.getNotes(),
                customer.getActive(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
