package co.leapwise.banking;

import co.leapwise.banking.common.AccountType;
import co.leapwise.banking.model.Account;
import co.leapwise.banking.model.Currency;
import co.leapwise.banking.model.Customer;
import co.leapwise.banking.model.Transaction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {
  public List<Transaction> getTransactions(Long... transactionAmount) {
    var customer =
        Customer.builder()
            .name("Jakov")
            .address("Ilica 1")
            .email("jakov@email.com")
            .phoneNumber("01000111")
            .build();

    var account1 =
        Account.builder()
            .accountNumber("HR1234")
            .accountType(AccountType.PERSONAL)
            .balance(1000000L)
            .customer(customer)
            .build();

    var account2 =
        Account.builder()
            .accountNumber("HR1234")
            .accountType(AccountType.PERSONAL)
            .balance(1000000L)
            .customer(customer)
            .build();

    customer.setAccounts(new ArrayList<>(List.of(account1, account2)));

    var currency = Currency.builder().name("EUR").build();

    var transactions =
            Arrays.stream(transactionAmount)
                .map(
                    (amount) ->
                        Transaction.builder()
                            .sender(account1)
                            .receiver(account2)
                            .amount(amount)
                            .currency(currency)
                            .message("This is a test")
                            .build())
                .toList();

    currency.setTransactions(new ArrayList<>(transactions));
    account1.setSenderTransactions(new ArrayList<>(transactions));
    account2.setReceiverTransactions(new ArrayList<>(transactions));

    return transactions;
  }

  public List<Transaction> getTransactionsFromLastMonth(Long... transactionAmount) {
    var transactions = getTransactions(transactionAmount);
    transactions.forEach(transaction -> transaction.setTimeStamp(LocalDateTime.now().minusMonths(1L)));
    return transactions;
  }
}
