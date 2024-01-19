package com.leapwise.bankingsystem.integration;

import com.leapwise.bankingsystem.domain.Transaction;
import com.leapwise.bankingsystem.repository.TransactionRepository;
import com.leapwise.bankingsystem.rest.dto.TransactionRequestDto;
import com.leapwise.bankingsystem.rest.dto.TransactionResponseDto;
import com.leapwise.bankingsystem.service.TransactionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static reactor.core.publisher.Mono.just;

public class TransactionControllerIT extends TestcontainersInitializer {

    private static final UUID CUSTOMER_ID = UUID.fromString("84cbef21-74ae-4a58-9250-edfa07cb657e");
    private static final String CURRENCY = "EUR";
    private static final LocalDate TIMESTAMP = LocalDate.of(2024, 1, 18);
    private static final BigDecimal AMOUNT = new BigDecimal("12734.14");
    private static final String RECEIVER_ACC_NUMBER = "ACC-8e4eb12c-b16a-4f9c-b0bb-2ad499d948af";

    private static final UUID RECEIVER_ACC_ID = UUID.fromString("acdf3f50-da57-4704-ade0-21be3b3d46e6");
    private static final UUID SENDER_ACC_ID = UUID.fromString("673ccf2a-c9d2-40e0-a198-a6f946d532fc");
    private static final String MESSAGE = "THIS IS A TRANSACTION MESSAGE.";

    @Autowired
    TransactionRepository repository;
    @Autowired
    TransactionService service;
    @Test
    @DisplayName("200 - GET /transactions/{customerId}")
    void getAllByCustomer() {
        List<TransactionResponseDto> actual = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.format("/transactions/%s", CUSTOMER_ID))
                        .queryParam("currency", CURRENCY)
                        .queryParam("timestamp", TIMESTAMP)
                        .queryParam("amount", AMOUNT)
                        .queryParam("receiverAccNumber", RECEIVER_ACC_NUMBER)
                        .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(new ParameterizedTypeReference<List<TransactionResponseDto>>() {}).returnResult()
                .getResponseBody();

        List<TransactionResponseDto> expected = service.getAllByCustomerId(CUSTOMER_ID, constructSearchParams());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("200 - POST /transactions")
    void create() throws MessagingException {
        TransactionRequestDto request = TransactionRequestDto.builder()
                .senderAccountId(SENDER_ACC_ID)
                .receiverAccountId(RECEIVER_ACC_ID)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .message(MESSAGE)
                .timestamp(TIMESTAMP)
                .build();

        UUID transactionId = webTestClient.post()
                .uri("/transactions")
                .body(just(request), TransactionRequestDto.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(UUID.class).returnResult()
                .getResponseBody();

        Transaction transaction = repository.findById(transactionId).get();

        assertEquals(request.getSenderAccountId(), transaction.getSenderAccount().getId());
        assertEquals(request.getReceiverAccountId(), transaction.getReceiverAccount().getId());
        assertEquals(request.getAmount(), transaction.getAmount());
        assertEquals(request.getCurrency(), transaction.getCurrency().getCurrencyCode());
        assertEquals(request.getMessage(), transaction.getMessage());
        assertEquals(request.getTimestamp(), transaction.getTimestamp());

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(2, receivedMessages.length);
        MimeMessage senderReceivedMessage = receivedMessages[0];
        assertEquals(1, senderReceivedMessage.getAllRecipients().length);
        assertEquals("johndoe@example.com", senderReceivedMessage.getAllRecipients()[0].toString());

        MimeMessage receiverReceivedMessage = receivedMessages[1];
        assertEquals(1, receiverReceivedMessage.getAllRecipients().length);
        assertEquals("janesmith@example.com", receiverReceivedMessage.getAllRecipients()[0].toString());
    }

    private Map<String, String> constructSearchParams() {
        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("currency", CURRENCY);
        searchParams.put("timestamp", TIMESTAMP.toString());
        searchParams.put("amount", AMOUNT.toString());
        searchParams.put("receiverAccNumber", RECEIVER_ACC_NUMBER);
        return searchParams;
    }
}
