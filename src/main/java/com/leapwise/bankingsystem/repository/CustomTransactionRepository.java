package com.leapwise.bankingsystem.repository;

import com.leapwise.bankingsystem.domain.Transaction;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CustomTransactionRepository {
    List<Transaction> findAllByCustomerId(UUID customerId, Map<String, String> params);
}
