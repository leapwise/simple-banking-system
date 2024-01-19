package com.leapwise.bankingsystem.mapper;

import com.leapwise.bankingsystem.domain.Customer;
import com.leapwise.bankingsystem.domain.Transaction;
import com.leapwise.bankingsystem.repository.AccountRepository;
import com.leapwise.bankingsystem.repository.TransactionRepository;
import com.leapwise.bankingsystem.rest.dto.TransactionResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class TransactionMapper {

    AccountRepository accountRepository;
    public TransactionResponseDto toDto(Transaction entity) {
        return TransactionResponseDto.builder()
                .id(entity.getId())
                .senderAccountNumber(entity.getSenderAccount().getAccountNumber())
                .receiverAccountNumber(entity.getReceiverAccount().getAccountNumber())
                .amount(entity.getAmount())
                .currency(entity.getCurrency().getDisplayName())
                .message(entity.getMessage())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public Transaction csvToEntity(CSVRecord record) {
        return Transaction.builder()
                .senderAccount(accountRepository.getReferenceById(UUID.fromString(requireNonNull(record.get("sender_account_id")))))
                .receiverAccount(accountRepository.getReferenceById(UUID.fromString(requireNonNull(record.get("receiver_account_id")))))
                .amount(new BigDecimal(getVal(record,"amount")))
                .currency(Currency.getInstance(getVal(record, "currency")))
                .message(getVal(record, "message"))
                .timestamp(LocalDate.parse(getVal(record, "timestamp")))
                .created(LocalDate.parse(getVal(record, "created")))
                .modified(LocalDate.parse(getVal(record, "modified")))
                .build();
    }

    private static String getVal(CSVRecord record, String key) {
        return requireNonNull(record.get(key));
    }
}
