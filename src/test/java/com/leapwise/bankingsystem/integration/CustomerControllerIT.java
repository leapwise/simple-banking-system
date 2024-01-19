package com.leapwise.bankingsystem.integration;

import com.leapwise.bankingsystem.mapper.AccountMapper;
import com.leapwise.bankingsystem.mapper.CustomerMapper;
import com.leapwise.bankingsystem.repository.AccountRepository;
import com.leapwise.bankingsystem.repository.CustomerRepository;
import com.leapwise.bankingsystem.rest.dto.AccountResponseDto;
import com.leapwise.bankingsystem.rest.dto.CustomerResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerControllerIT extends TestcontainersInitializer {

    private static final UUID CUSTOMER_ID = UUID.fromString("84cbef21-74ae-4a58-9250-edfa07cb657e");
    @Autowired
    CustomerRepository repository;
    @Autowired
    CustomerMapper mapper;

    @Test
    @DisplayName("200 - GET /customers/{customerId}")
    void getById() {
        CustomerResponseDto actual = webTestClient.get()
                .uri(String.format("/customers/%s", CUSTOMER_ID))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(CustomerResponseDto.class)
                .returnResult().getResponseBody();

        CustomerResponseDto expected = mapper.toDto(repository.findById(CUSTOMER_ID).get());

        assertEquals(expected, actual);
    }
}
