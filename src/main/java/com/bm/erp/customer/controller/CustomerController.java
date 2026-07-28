package com.bm.erp.customer.controller;

import com.bm.erp.customer.dto.CustomerRequest;
import com.bm.erp.customer.dto.CustomerResponse;
import com.bm.erp.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable UUID id) {
        CustomerResponse customerResponse = customerService.getById(id);
        return ResponseEntity.ok(customerResponse);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> save
            (@Valid @RequestBody CustomerRequest customerRequest) {

        CustomerResponse customerResponse = customerService.save(customerRequest);

        return ResponseEntity.ok(customerResponse);

    }
}
