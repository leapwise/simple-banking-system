package com.leapwise.bankingsystem.mapper;

import com.leapwise.bankingsystem.domain.Account;
import com.leapwise.bankingsystem.rest.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AccountMapper {

    CustomerMapper customerMapper;

    public AccountResponseDto toDto(Account entity) {
        return AccountResponseDto.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .accountType(entity.getAccountType())
                .balance(entity.getBalance())
                .pastMonthTurnover(entity.getPastMonthTurnover())
                .created(entity.getCreated())
                .modified(entity.getModified())
                .customer(customerMapper.toDto(entity.getCustomer()))
                .build();
    }
}
