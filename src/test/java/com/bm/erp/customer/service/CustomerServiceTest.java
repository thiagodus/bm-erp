package com.bm.erp.customer.service;

import com.bm.erp.customer.dto.CustomerRequest;
import com.bm.erp.customer.dto.CustomerResponse;
import com.bm.erp.customer.entity.Customer;
import com.bm.erp.customer.entity.CustomerType;
import com.bm.erp.customer.exception.CustomerAlreadyExistsException;
import com.bm.erp.customer.exception.CustomerNotFoundException;
import com.bm.erp.customer.mapper.CustomerMapper;
import com.bm.erp.customer.repository.CustomerRepository;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    CustomerMapper mapper;

    @InjectMocks
    CustomerService service;

    private static @NonNull CustomerResponse getResponse() {
        return new CustomerResponse(
                UUID.randomUUID(),
                CustomerType.INDIVIDUAL,
                "João da Silva",
                "41999999999",
                null,
                "joao@email.com",
                "Rua Example, 123",
                "Curitiba",
                "PR",
                "80000000",
                "BR",
                null,
                true,
                Instant.now(),
                Instant.now()
        );
    }

    private static @NonNull CustomerRequest getRequest() {
        return new CustomerRequest(
                CustomerType.INDIVIDUAL,
                "João da Silva",
                "41999999999",
                null,
                "joao@email.com",
                "Rua Example, 123",
                "Curitiba",
                "PR",
                "80000000",
                "BR",
                null
        );
    }

    private static Customer getCustomer(UUID id) {

        Customer customer = new Customer();
        customer.setId(id);
        customer.setCity("Rua Example, 123");
        customer.setDocument(null);
        customer.setName("João da Silva");
        customer.setPhone("41999999999");
        customer.setNotes(null);
        customer.setType(CustomerType.INDIVIDUAL);
        customer.setEmail("joao@email.com");
        customer.setCountry("BR");
        customer.setState("PR");
        customer.setZipCode("80000000");
        return customer;
    }

    @Test
    void shouldCreateCustomerWhenPhoneDoesNotExist() {

        //Arrange
        CustomerRequest request = getRequest();

        Customer customer = new Customer();

        when(customerRepository.findByPhone(request.phone())).thenReturn(Optional.empty());
        when(mapper.toEntity(request)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = getResponse();

        when(mapper.toResponse(customer)).thenReturn(response);

        //Act
        CustomerResponse customerResponse = service.save(request);

        //Assert
        assertThat(customerResponse).isEqualTo(response);

        verify(customerRepository).findByPhone(request.phone());
        verify(mapper).toEntity(request);
        verify(customerRepository).save(customer);
        verify(mapper).toResponse(customer);

    }


    @Test
    void shouldThrowExceptionWhenPhoneAlreadyExists(){
        //Arrange
        CustomerRequest request = getRequest();
        Customer existingCustomer = new Customer();
        when(customerRepository.findByPhone(request.phone())).thenReturn(Optional.of(existingCustomer));

        //Act //Assert

        assertThatThrownBy(() -> service.save(request)).isInstanceOf(CustomerAlreadyExistsException.class);
        verify(customerRepository).findByPhone(request.phone());
        verify(mapper, never()).toEntity(any(CustomerRequest.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist(){
        //Arrange
        UUID uuid = UUID.randomUUID();
        when(customerRepository.findById(uuid)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> service.getById(uuid)).isInstanceOf(CustomerNotFoundException.class);
        verify(customerRepository).findById(uuid);
    }

    @Test
    void shouldReturnCustomerWhenIdExists(){
        //Arrange
         UUID uuid = UUID.randomUUID();
         Customer customer = getCustomer(uuid);
         CustomerResponse response = getResponse();

         when(customerRepository.findById(uuid)).thenReturn(Optional.of(customer));
         when(mapper.toResponse(customer)).thenReturn(response);

        //Act
        CustomerResponse customerResponse = service.getById(uuid);

        //Assert
        assertThat(customerResponse).isEqualTo(response);

        verify(customerRepository).findById(uuid);
        verify(mapper).toResponse(customer);

    }



}
