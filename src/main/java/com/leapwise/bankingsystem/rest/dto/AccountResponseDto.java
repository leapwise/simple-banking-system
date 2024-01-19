package com.leapwise.bankingsystem.rest.dto;

import com.leapwise.bankingsystem.domain.AccountType;
import com.leapwise.bankingsystem.domain.Customer;
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
public class AccountResponseDto {
    UUID id;
    String accountNumber;
    AccountType accountType;
    BigDecimal balance;
    BigDecimal pastMonthTurnover;
    LocalDate created;
    LocalDate modified;
    CustomerResponseDto customer;
}
