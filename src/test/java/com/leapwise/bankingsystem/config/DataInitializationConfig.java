package com.leapwise.bankingsystem.config;

import com.leapwise.bankingsystem.domain.Transaction;
import com.leapwise.bankingsystem.mapper.TransactionMapper;
import com.leapwise.bankingsystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DataInitializationConfig implements ApplicationListener<ApplicationReadyEvent> {

    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;

    private void initTransactions() throws IOException {
        List<Transaction> transactionList = List.of();
        Path path = Paths.get("src/test/resources/data/transaction.csv");

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            Iterable<CSVRecord> iterable = CSVFormat.RFC4180.withFirstRecordAsHeader()
                    .parse(reader);

            transactionList = StreamSupport
                    .stream(iterable.spliterator(), false)
                    .map(transactionMapper::csvToEntity)
                    .toList();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        transactionList.forEach(transactionRepository::save);
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            initTransactions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


