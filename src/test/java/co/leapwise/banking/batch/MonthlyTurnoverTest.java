package co.leapwise.banking.batch;

import static org.junit.jupiter.api.Assertions.*;

import co.leapwise.banking.TestUtils;
import co.leapwise.banking.manager.AccountManager;
import co.leapwise.banking.manager.MTransactionManager;
import co.leapwise.banking.model.Account;
import co.leapwise.banking.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@SpringBatchTest
class MonthlyTurnoverTest {
  @Autowired JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired MTransactionManager transactionManager;

  @Autowired AccountManager accountManager;

  @MockBean EmailService emailService;

  @Test
  @DirtiesContext
  void monthlyTurnoverJob_rowsFromLastMonthExistInDatabase_turnoverGetsWritten() throws Exception {
    var transactions = TestUtils.getTransactionsFromLastMonth(1000L, 2000L, 3000L);
    transactionManager.insertTransactions(transactions);

    var jobExecution = jobLauncherTestUtils.launchJob();

    var accounts = accountManager.getAll();

    assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    accounts.stream()
        .map(Account::getPastMonthTurnover)
        .forEach(
            turnover -> {
              assertNotNull(turnover);
              assertNotEquals(0, turnover);
            });
  }

  @Test
  @DirtiesContext
  void monthlyTurnoverJob_rowsFromLastMonthDontExistInDatabase_turnoverDoesntGetWritten()
      throws Exception {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    transactionManager.insertTransactions(transactions);

    var jobExecution = jobLauncherTestUtils.launchJob();

    var accounts = accountManager.getAll();

    assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    accounts.stream()
        .map(Account::getPastMonthTurnover)
        .forEach(turnover -> assertEquals(0, turnover));
  }
}
