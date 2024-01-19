package com.leapwise.bankingsystem.mapper;

import com.leapwise.bankingsystem.domain.Account;
import com.leapwise.bankingsystem.domain.Customer;
import com.leapwise.bankingsystem.rest.dto.CustomerResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class CustomerMapper {

    public CustomerResponseDto toDto(Customer entity) {
        return CustomerResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .accountIds(getAccountIds(entity))
                .balance(calculateBalance(entity))
                .build();
    }

    private BigDecimal calculateBalance(Customer entity) {
        return entity.getAccounts()
                .stream()
                .map(Account::getBalance)
                .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private List<UUID> getAccountIds(Customer entity) {
        return entity.getAccounts()
                .stream()
                .map(Account::getId)
                .toList();
    }
}
