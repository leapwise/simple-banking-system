package co.leapwise.banking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import co.leapwise.banking.TestUtils;
import co.leapwise.banking.manager.AccountManager;
import co.leapwise.banking.manager.CurrencyManager;
import co.leapwise.banking.manager.MTransactionManager;
import co.leapwise.banking.model.Transaction;
import co.leapwise.banking.request.TransactionRequest;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
  @Mock private MTransactionManager transactionManager;
  @Mock private AccountManager accountManager;
  @Mock private CurrencyManager currencyManager;
  @Mock private CurrencyConverter currencyConverter;
  @Mock private EmailService emailService;
  @Mock private ModelMapper modelMapper;

  @InjectMocks private TransactionService transactionService;

  @Captor private ArgumentCaptor<Transaction> captor;

  @Test
  void insertTransaction_senderDifferentFromReceiverAndAmountOk_allOk() {
    var transaction = TestUtils.getTransactions(1000L).get(0);

    var sender = transaction.getSender();
    sender.setAccountId(1L);
    var senderOldBalance = sender.getBalance();

    var receiver = transaction.getReceiver();
    receiver.setAccountId(2L);
    var receiverOldBalance = receiver.getBalance();

    var currency = transaction.getCurrency();
    currency.setCurrencyId(1L);

    var request =
        TransactionRequest.builder()
            .senderId(sender.getAccountId())
            .receiverId(receiver.getAccountId())
            .amount(transaction.getAmount())
            .currencyId(currency.getCurrencyId())
            .message(transaction.getMessage())
            .build();

    when(accountManager.getAccount(sender.getAccountId())).thenReturn(sender);
    when(accountManager.getAccount(receiver.getAccountId())).thenReturn(receiver);
    when(currencyManager.getCurrency(currency.getCurrencyId())).thenReturn(currency);
    when(currencyConverter.convert(any(), any(), any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    transactionService.insertTransaction(request);

    verify(emailService, times(1))
        .sendTransactionSuccessConfirmation(any(Transaction.class), anyLong(), anyLong());
    verify(transactionManager, times(1)).insertTransaction(captor.capture());

    assertEquals(
        senderOldBalance - transaction.getAmount(), captor.getValue().getSender().getBalance());
    assertEquals(
        receiverOldBalance + transaction.getAmount(), captor.getValue().getReceiver().getBalance());
  }

  @Test
  void insertTransaction_senderSameAsReceiver_throwsError() {
    var transaction = TestUtils.getTransactions(1000L).get(0);

    var sender = transaction.getSender();
    sender.setAccountId(1L);

    var receiver = transaction.getReceiver();
    receiver.setAccountId(1L);

    var currency = transaction.getCurrency();
    currency.setCurrencyId(1L);

    var request =
        TransactionRequest.builder()
            .senderId(sender.getAccountId())
            .receiverId(receiver.getAccountId())
            .amount(transaction.getAmount())
            .currencyId(currency.getCurrencyId())
            .message(transaction.getMessage())
            .build();

    when(accountManager.getAccount(sender.getAccountId())).thenReturn(sender);
    when(accountManager.getAccount(receiver.getAccountId())).thenReturn(receiver);
    when(currencyManager.getCurrency(currency.getCurrencyId())).thenReturn(currency);
    when(currencyConverter.convert(any(), any(), any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    assertThrows(IllegalArgumentException.class, () -> transactionService.insertTransaction(request));

    verify(emailService, times(1)).sendTransactionFailConfirmation(sender.getCustomer().getEmail());
    verify(transactionManager, times(0)).insertTransaction(any(Transaction.class));
  }

  @Test
  void insertTransaction_insufficientAccountBalance_throwsError() {
    var transaction = TestUtils.getTransactions(1L).get(0);
    transaction.setAmount(transaction.getSender().getBalance() + 1L);

    var sender = transaction.getSender();
    sender.setAccountId(1L);

    var receiver = transaction.getReceiver();
    receiver.setAccountId(2L);

    var currency = transaction.getCurrency();
    currency.setCurrencyId(1L);

    var request =
        TransactionRequest.builder()
            .senderId(sender.getAccountId())
            .receiverId(receiver.getAccountId())
            .amount(transaction.getAmount())
            .currencyId(currency.getCurrencyId())
            .message(transaction.getMessage())
            .build();

    when(accountManager.getAccount(sender.getAccountId())).thenReturn(sender);
    when(accountManager.getAccount(receiver.getAccountId())).thenReturn(receiver);
    when(currencyManager.getCurrency(currency.getCurrencyId())).thenReturn(currency);
    when(currencyConverter.convert(any(), any(), any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    assertThrows(IllegalStateException.class, () -> transactionService.insertTransaction(request));

    verify(emailService, times(1)).sendTransactionFailConfirmation(sender.getCustomer().getEmail());
    verify(transactionManager, times(0)).insertTransaction(any(Transaction.class));
  }

  @Test
  void getTransactionsByCustomer_multipleTransactions_allOk() {
    var transactionsList = TestUtils.getTransactions(1000L, 2000L);
    var transactionsPage = new PageImpl<>(transactionsList, PageRequest.of(0, 10), 2);

    when(transactionManager.getByCustomerId(any(), any())).thenReturn(transactionsPage);

    var result = transactionService.getTransactionsByCustomer(null, null);

    verify(modelMapper, times(2)).map(any(), any());
    assertEquals(2, result.getTotal());
  }

  @Test
  void getTransactionsByCustomer_noTransactions_allOk() {
    var transactionsList = new ArrayList<Transaction>();
    var transactionsPage = new PageImpl<>(transactionsList, PageRequest.of(0, 10), 0);

    when(transactionManager.getByCustomerId(any(), any())).thenReturn(transactionsPage);

    var result = transactionService.getTransactionsByCustomer(null, null);

    verify(modelMapper, times(0)).map(any(), any());
    assertEquals(0, result.getTotal());
  }
}
