package com.leapwise.bankingsystem.service;

import com.leapwise.bankingsystem.domain.Account;
import com.leapwise.bankingsystem.domain.Customer;
import com.leapwise.bankingsystem.domain.Transaction;
import com.leapwise.bankingsystem.mapper.TransactionMapper;
import com.leapwise.bankingsystem.repository.TransactionRepository;
import com.leapwise.bankingsystem.rest.dto.CustomerResponseDto;
import com.leapwise.bankingsystem.rest.dto.TransactionRequestDto;
import com.leapwise.bankingsystem.rest.dto.TransactionResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final UUID SENDER_ACC_ID = UUID.randomUUID();
    private static final UUID RECEIVER_ACC_ID = UUID.randomUUID();
    @Mock
    AccountService accountService;
    @Mock
    MailService mailService;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    TransactionMapper transactionMapper;
    @Mock
    Account senderAccount;
    @Mock
    Account receiverAccount;
    @Mock
    TransactionRequestDto requestDto;
    @Mock
    Customer customer;
    @Mock
    Transaction transaction;
    @Mock
    TransactionResponseDto responseDto;

    @InjectMocks
    TransactionService service;
    @Test
    void create() {
        given(senderAccount.getBalance()).willReturn(new BigDecimal("2500"));
        given(receiverAccount.getBalance()).willReturn(new BigDecimal("1500"));
        given(accountService.getById(SENDER_ACC_ID)).willReturn(senderAccount);
        given(accountService.getById(RECEIVER_ACC_ID)).willReturn(receiverAccount);
        given(transaction.getId()).willReturn(UUID.randomUUID());
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);

        service.create(createRequest());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountService, times(1)).save(senderAccount);
        verify(accountService, times(1)).save(receiverAccount);
        verify(mailService, times(2)).send(any());
    }

    private TransactionRequestDto createRequest() {
        return TransactionRequestDto.builder()
                .senderAccountId(SENDER_ACC_ID)
                .receiverAccountId(RECEIVER_ACC_ID)
                .amount(new BigDecimal("1000"))
                .currency("EUR")
                .timestamp(LocalDate.now())
                .message("MESSAGE")
                .build();
    }

    @Test
    void getAllByCustomerId() {
        given(transactionRepository.findAllByCustomerId(customer.getId(), Map.of()))
                .willReturn(List.of(transaction));
        given(transactionMapper.toDto(transaction)).willReturn(responseDto);

        List<TransactionResponseDto> result = service.getAllByCustomerId(customer.getId(), Map.of());

        assertEquals(responseDto, result.get(0));
    }
}