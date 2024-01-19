package com.leapwise.bankingsystem.rest.controller;

import com.leapwise.bankingsystem.rest.dto.CustomerResponseDto;
import com.leapwise.bankingsystem.service.CustomerService;
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
@RequestMapping("/customers")
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CustomerController {

    CustomerService service;

    @GetMapping("{id}")
    @ResponseStatus(OK)
    public CustomerResponseDto getById(@PathVariable UUID id) {
        return service.getById(id);
    }
}
