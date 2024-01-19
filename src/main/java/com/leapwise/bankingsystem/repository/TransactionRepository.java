package com.leapwise.bankingsystem.repository;

import com.leapwise.bankingsystem.domain.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, CustomTransactionRepository {

    @Query(value = "SELECT SUM(t.amount) as total_amount\n" +
            "    FROM transaction t\n" +
            "    WHERE t.timestamp BETWEEN :from AND :to\n" +
            "    AND t.sender_account_id = :senderId",
    nativeQuery = true)
    BigDecimal calculateExpensesForPastMonth(@Param("from") LocalDate from,
                                             @Param("to")LocalDate to,
                                             @Param("senderId") UUID senderId);
    @Query(value = "SELECT SUM(t.amount)\n" +
            "    FROM transaction t\n" +
            "    WHERE t.timestamp BETWEEN :from AND :to\n" +
            "    AND t.receiver_account_id = :receiverId",
            nativeQuery = true)
    BigDecimal calculateIncomeForPastMonth(LocalDate from, LocalDate to, UUID receiverId);
}
