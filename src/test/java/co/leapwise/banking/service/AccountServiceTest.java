package co.leapwise.banking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import co.leapwise.banking.TestUtils;
import co.leapwise.banking.manager.MTransactionManager;
import co.leapwise.banking.model.Transaction;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock private MTransactionManager transactionManager;

  @InjectMocks private AccountService accountService;

  @Test
  void calculatePastMonthTurnover_transactionsExist_calculationOk() {
    var pastMonth = YearMonth.now().minus(1, ChronoUnit.MONTHS);
    var transactions = TestUtils.getTransactionsFromLastMonth(1000L, 2000L);

    var testedAccountId = transactions.get(0).getSender().getAccountId();

    when(transactionManager.getByAccountIdAndYearMonth(testedAccountId, pastMonth))
        .thenReturn(transactions);

    var result = accountService.calculatePastMonthTurnover(testedAccountId);

    assertEquals(3000L, result);
  }

  @Test
  void calculatePastMonthTurnover_noTransactions_calculationZero() {
    var pastMonth = YearMonth.now().minus(1, ChronoUnit.MONTHS);
    var transactions = new ArrayList<Transaction>();

    var testedAccountId = 1L;

    when(transactionManager.getByAccountIdAndYearMonth(testedAccountId, pastMonth))
        .thenReturn(transactions);

    var result = accountService.calculatePastMonthTurnover(testedAccountId);

    assertEquals(0L, result);
  }
}
