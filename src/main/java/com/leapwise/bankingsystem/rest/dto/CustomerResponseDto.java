package com.leapwise.bankingsystem.rest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Builder
@Data
@FieldDefaults(level = PRIVATE)
public class CustomerResponseDto {

    UUID id;
    String name;
    String address;
    String email;
    String phoneNumber;
    List<UUID> accountIds;
    BigDecimal balance;
}
