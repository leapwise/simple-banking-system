package com.leapwise.bankingsystem.service;

import com.leapwise.bankingsystem.domain.Account;
import com.leapwise.bankingsystem.repository.AccountRepository;
import com.leapwise.bankingsystem.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AccountService {

    AccountRepository repository;
    TransactionRepository transactionRepository;

    public Account getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Account with id = %s not found!", id)));
    }

    public void save(Account entity) {
        repository.save(entity);
    }

    public void calculatePastMonthTurnover() {
        LocalDate currentDate = LocalDate.now();
        LocalDate from = currentDate.minusMonths(1)
                .with(TemporalAdjusters.firstDayOfMonth());
        LocalDate to = currentDate.with(TemporalAdjusters.firstDayOfMonth());

        for(Account account : repository.findAll()) {
            BigDecimal income = transactionRepository.calculateIncomeForPastMonth(from, to, account.getId());
            BigDecimal expenses = transactionRepository.calculateExpensesForPastMonth(from, to, account.getId());

            BigDecimal pastMonthTurnOver = income.subtract(expenses);

            account.setPastMonthTurnover(pastMonthTurnOver);

            repository.save(account);
        }
    }
}
