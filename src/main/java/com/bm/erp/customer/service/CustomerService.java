package com.bm.erp.customer.service;

import com.bm.erp.customer.dto.CustomerRequest;
import com.bm.erp.customer.dto.CustomerResponse;
import com.bm.erp.customer.entity.Customer;
import com.bm.erp.customer.exception.CustomerAlreadyExistsException;
import com.bm.erp.customer.exception.CustomerNotFoundException;
import com.bm.erp.customer.mapper.CustomerMapper;
import com.bm.erp.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerResponse save(CustomerRequest request) {
        Optional<Customer> existingCustomer = customerRepository.findByPhone(request.phone());
        if (existingCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException(request.phone());
        }

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);

    }

    public CustomerResponse getById(UUID id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse).
                orElseThrow(CustomerNotFoundException::new);
    }
}
