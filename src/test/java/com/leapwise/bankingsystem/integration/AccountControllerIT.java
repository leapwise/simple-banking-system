package com.leapwise.bankingsystem.integration;

import com.leapwise.bankingsystem.mapper.AccountMapper;
import com.leapwise.bankingsystem.repository.AccountRepository;
import com.leapwise.bankingsystem.rest.dto.AccountResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AccountControllerIT extends TestcontainersInitializer {

    private static final UUID ACCOUNT_ID = UUID.fromString("673ccf2a-c9d2-40e0-a198-a6f946d532fc");
    @Autowired
    AccountRepository repository;
    @Autowired
    AccountMapper mapper;

    @Test
    @DisplayName("200 - GET /accounts/{accountId}")
    void getById() {
        AccountResponseDto actual = webTestClient.get()
                .uri(String.format("/accounts/%s", ACCOUNT_ID))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(AccountResponseDto.class)
                .returnResult().getResponseBody();

        AccountResponseDto expected = mapper.toDto(repository.findById(ACCOUNT_ID).get());

        assertEquals(expected, actual);
    }
}
