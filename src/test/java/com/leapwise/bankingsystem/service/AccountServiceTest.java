package com.leapwise.bankingsystem.service;

import com.leapwise.bankingsystem.domain.Account;
import com.leapwise.bankingsystem.repository.AccountRepository;
import com.leapwise.bankingsystem.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    private static final UUID ACCOUNT_ID = UUID.randomUUID();

    @Mock
    AccountRepository repository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    Account account;
    @Captor
    ArgumentCaptor<Account> accountArgumentCaptor;
    @InjectMocks
    AccountService service;

    @Test
    void getById() {
        given(repository.findById(ACCOUNT_ID)).willReturn(Optional.of(account));

        Account actual = service.getById(ACCOUNT_ID);

        assertEquals(account, actual);
    }

    @Test
    void getById_notFound_throwsException() {
        given(repository.findById(ACCOUNT_ID)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(ACCOUNT_ID));
    }

    @Test
    void save() {
        service.save(account);

        verify(repository, times(1)).save(account);
    }

    @Test
    void calculatePastMonthTurnover() {
        Account entity = Account.builder()
                .id(ACCOUNT_ID)
                .build();

        given(repository.findAll()).willReturn(List.of(entity));
        given(transactionRepository.calculateIncomeForPastMonth(any(), any(), eq(ACCOUNT_ID)))
                .willReturn(BigDecimal.valueOf(100L));
        given(transactionRepository.calculateExpensesForPastMonth(any(), any(), eq(ACCOUNT_ID)))
                .willReturn(BigDecimal.valueOf(50L));

        service.calculatePastMonthTurnover();

        verify(repository).save(accountArgumentCaptor.capture());

        assertEquals(accountArgumentCaptor.getValue().getPastMonthTurnover(), BigDecimal.valueOf(50L));
    }
}