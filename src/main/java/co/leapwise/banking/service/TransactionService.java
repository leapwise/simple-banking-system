package co.leapwise.banking.service;

import co.leapwise.banking.common.Const;
import co.leapwise.banking.manager.AccountManager;
import co.leapwise.banking.manager.CurrencyManager;
import co.leapwise.banking.manager.MTransactionManager;
import co.leapwise.banking.model.Account;
import co.leapwise.banking.model.Transaction;
import co.leapwise.banking.request.TransactionRequest;
import co.leapwise.banking.request.params.TransactionParams;
import co.leapwise.banking.response.PageableResponse;
import co.leapwise.banking.response.TransactionResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
  private final MTransactionManager transactionManager;
  private final AccountManager accountManager;
  private final CurrencyManager currencyManager;
  private final CurrencyConverter currencyConverter;
  private final EmailService emailService;
  private final ModelMapper modelMapper;

  public Long insertTransaction(TransactionRequest request) {
    var sender = accountManager.getAccount(request.getSenderId());
    var receiver = accountManager.getAccount(request.getReceiverId());
    var currency = currencyManager.getCurrency(request.getCurrencyId());
    var senderOldBalance = sender.getBalance();
    var receiverOldBalance = receiver.getBalance();

    try {
      var amount =
          currencyConverter.convert(
              request.getAmount(), currency.getCurrencyId(), Const.MAIN_CURRENCY_ID);
      validateSenderAndReceiver(sender, receiver, amount);
      transferBalance(sender, receiver, amount);

      var transaction =
          Transaction.builder()
              .sender(sender)
              .receiver(receiver)
              .amount(request.getAmount())
              .currency(currency)
              .message(request.getMessage())
              .build();

      sender.getSenderTransactions().add(transaction);
      receiver.getReceiverTransactions().add(transaction);
      currency.getTransactions().add(transaction);

      transactionManager.insertTransaction(transaction);

      emailService.sendTransactionSuccessConfirmation(
          transaction, senderOldBalance, receiverOldBalance);

      return transaction.getTransactionId();
    } catch (RuntimeException e) {
      emailService.sendTransactionFailConfirmation(sender.getCustomer().getEmail());
      throw e;
    }
  }

  private void validateSenderAndReceiver(Account sender, Account receiver, Long amount) {
    if (Objects.equals(sender.getAccountId(), receiver.getAccountId())) {
      throw new IllegalArgumentException("Sender same as receiver.");
    }

    if (amount > sender.getBalance()) {
      throw new IllegalStateException("Insufficient account balance.");
    }
  }

  private void transferBalance(Account sender, Account receiver, Long amount) {
    var senderBalance = sender.getBalance() - amount;
    var receiverBalance = receiver.getBalance() + amount;
    sender.setBalance(senderBalance);
    receiver.setBalance(receiverBalance);
  }

  public PageableResponse<TransactionResponse> getTransactionsByCustomer(
      Long customerId, TransactionParams params) {
    var transactions = transactionManager.getByCustomerId(customerId, params);

    return PageableResponse.<TransactionResponse>builder()
        .total(transactions.getTotalElements())
        .data(
            transactions.stream().map(t -> modelMapper.map(t, TransactionResponse.class)).toList())
        .build();
  }
}
