package com.leapwise.bankingsystem.service;

import com.leapwise.bankingsystem.domain.Customer;
import com.leapwise.bankingsystem.mapper.CustomerMapper;
import com.leapwise.bankingsystem.repository.CustomerRepository;
import com.leapwise.bankingsystem.rest.dto.CustomerResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomerService {

    CustomerRepository repository;
    CustomerMapper mapper;

    public CustomerResponseDto getById(UUID id) {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Customer with id = %s not found!", id)));

        return mapper.toDto(entity);
    }
}
