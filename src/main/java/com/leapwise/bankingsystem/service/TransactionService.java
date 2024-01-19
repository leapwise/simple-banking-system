package com.leapwise.bankingsystem.service;

import com.leapwise.bankingsystem.domain.Account;
import com.leapwise.bankingsystem.domain.Transaction;
import com.leapwise.bankingsystem.mapper.TransactionMapper;
import com.leapwise.bankingsystem.repository.TransactionRepository;
import com.leapwise.bankingsystem.rest.dto.TransactionRequestDto;
import com.leapwise.bankingsystem.rest.dto.TransactionResponseDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TransactionService {

    AccountService accountService;
    MailService mailService;
    TransactionRepository repository;
    TransactionMapper mapper;

    public UUID create(TransactionRequestDto request) {
        Transaction current = null;
        List<MimeMessage> messages = new ArrayList<>();

        Account sender = accountService.getById(request.getSenderAccountId());
        Account receiver = accountService.getById(request.getReceiverAccountId());
        BigDecimal senderBalance = sender.getBalance();
        BigDecimal receiverBalance = receiver.getBalance();

       try {
            Currency currency = Currency.getInstance(request.getCurrency());

            var entity = Transaction.builder()
                    .amount(request.getAmount())
                    .currency(currency)
                    .message(request.getMessage())
                    .timestamp(request.getTimestamp() != null
                            ? request.getTimestamp()
                            : LocalDate.now())
                    .senderAccount(sender)
                    .receiverAccount(receiver)
                    .build();

            current = repository.save(entity);

            sender.setBalance(sender.getBalance().subtract(request.getAmount()));
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));

            accountService.save(sender);
            accountService.save(receiver);

            messages.add(mailService.buildMessage(current, sender, true, senderBalance));
            messages.add(mailService.buildMessage(current, receiver, true, receiverBalance));
        } catch (Exception e) {
            messages.add(mailService.buildMessage(current, sender, false, senderBalance));
            messages.add(mailService.buildMessage(current, receiver, false, receiverBalance));
       }

        messages.forEach(mailService::send);

        if(current == null) {
            throw new RuntimeException("Transaction was not properly processed!");
        }
        return current.getId();
    }

    public List<TransactionResponseDto> getAllByCustomerId(UUID customerId, Map<String, String> params) {
        return repository.findAllByCustomerId(customerId, params)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
