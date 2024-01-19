package com.leapwise.bankingsystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class TransactionRequestDto {
    UUID senderAccountId;
    UUID receiverAccountId;
    BigDecimal amount;
    String currency;
    String message;
    LocalDate timestamp;
}
