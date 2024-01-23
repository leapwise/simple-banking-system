package co.leapwise.banking.dev;

import co.leapwise.banking.common.Properties;
import co.leapwise.banking.manager.AccountManager;
import co.leapwise.banking.manager.CurrencyManager;
import co.leapwise.banking.manager.CustomerManager;
import co.leapwise.banking.manager.MTransactionManager;
import co.leapwise.banking.model.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Slf4j
public class DbPopulatorComponent {
  private final GeneratorService generatorService;
  private final AccountManager accountManager;
  private final CurrencyManager currencyManager;
  private final CustomerManager customerManager;
  private final MTransactionManager MTransactionManager;
  private final Properties properties;

  @EventListener
  @Transactional
  public void appReady(ApplicationReadyEvent event) {
    var customer = generatorService.generateCustomer();
    customerManager.insertCustomer(customer);
    log.info("Customers inserted");

    var accounts = generatorService.generateAccounts(customer);
    accountManager.insertAccounts(accounts);
    log.info("Accounts inserted");

    var currencies = generatorService.generateCurrencies();
    currencyManager.insertCurrencies(currencies);
    log.info("Currencies inserted");

    // Generate file
    generatorService.generateAndSaveRandomTransactions(
        properties.numberOfTransactions(), properties.generatedFileName(), accounts, currencies);
    log.info("Transactions file generated");

    // Read file
    var serializedTransactions = generatorService.readTransactions(properties.generatedFileName());
    var transactions =
        serializedTransactions.stream().map(this::mapSerializedTransactionToTransaction).toList();
    log.info("Transactions file read");

    // Write to db
    MTransactionManager.insertTransactions(transactions);
    log.info("Transactions inserted");
  }

  private Transaction mapSerializedTransactionToTransaction(
      SerializedTransaction serializedTransaction) {
    return Transaction.builder()
        .sender(accountManager.getAccount(serializedTransaction.getSenderId()))
        .receiver(accountManager.getAccount(serializedTransaction.getReceiverId()))
        .amount(serializedTransaction.getAmount())
        .currency(currencyManager.getCurrency(serializedTransaction.getCurrencyId()))
        .message(serializedTransaction.getMessage())
        .timeStamp(serializedTransaction.getTimeStamp())
        .build();
  }
}
