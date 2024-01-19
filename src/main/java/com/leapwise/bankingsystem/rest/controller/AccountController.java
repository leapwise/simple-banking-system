package com.leapwise.bankingsystem.rest.controller;

import com.leapwise.bankingsystem.mapper.AccountMapper;
import com.leapwise.bankingsystem.rest.dto.AccountResponseDto;
import com.leapwise.bankingsystem.rest.dto.CustomerResponseDto;
import com.leapwise.bankingsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AccountController {
    AccountService service;
    AccountMapper mapper;

    @GetMapping("{id}")
    @ResponseStatus(OK)
    public AccountResponseDto getById(@PathVariable UUID id) {
        return mapper.toDto(service.getById(id));
    }
}
