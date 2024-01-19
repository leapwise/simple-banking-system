package com.leapwise.bankingsystem.rest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Builder
@Data
@FieldDefaults(level = PRIVATE)
public class TransactionResponseDto {

    UUID id;
    String senderAccountNumber;
    String receiverAccountNumber;
    BigDecimal amount;
    String currency;
    String message;
    LocalDate timestamp;
}
