package com.leapwise.bankingsystem.rest.controller;

import com.leapwise.bankingsystem.rest.dto.TransactionRequestDto;
import com.leapwise.bankingsystem.rest.dto.TransactionResponseDto;
import com.leapwise.bankingsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class TransactionController {

    TransactionService service;
    @PostMapping
    @ResponseStatus(CREATED)
    public UUID create(@Valid @RequestBody TransactionRequestDto transactionRequest) {
        return service.create(transactionRequest);
    }

    @GetMapping("{id}")
    @ResponseStatus(OK)
    public List<TransactionResponseDto> getAllByCustomer(@PathVariable UUID id,
                                                         @RequestParam Map<String, String> searchParams) {
        return service.getAllByCustomerId(id,searchParams);
    }
}
