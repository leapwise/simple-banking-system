package co.leapwise.banking.repository;

import static org.junit.jupiter.api.Assertions.*;

import co.leapwise.banking.TestUtils;
import co.leapwise.banking.request.params.TransactionParams;
import java.time.Month;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TransactionRepositoryTest {
  @Autowired TransactionRepository repository;

  @Test
  void findByCustomerId_repositoryEmpty_noResult() {
    var params = new TransactionParams();

    var result = repository.findByCustomerId(1L, params, params.getPageRequest());

    assertEquals(result.getTotalPages(), 0);
    assertEquals(result.getTotalElements(), 0);
    assertTrue(result.isEmpty());
  }

  @Test
  void findByCustomerId_repositoryFilledAndNoExtraParams_containsResults() {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    repository.saveAll(transactions);
    var params = new TransactionParams();

    var testedTransaction = transactions.get(0);

    var result =
        repository.findByCustomerId(
            testedTransaction.getSender().getCustomer().getCustomerId(),
            params,
            params.getPageRequest());

    assertEquals(result.getTotalPages(), 1);
    assertEquals(result.getTotalElements(), 3);
    assertFalse(result.isEmpty());
  }

  @Test
  void findByCustomerId_repositoryFilledAndAmountParamUsed_containsResults() {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    repository.saveAll(transactions);
    var params = new TransactionParams();
    params.setAmount(2000L);

    var testedTransaction = transactions.get(0);

    var result =
        repository.findByCustomerId(
            testedTransaction.getSender().getCustomer().getCustomerId(),
            params,
            params.getPageRequest());

    assertEquals(result.getTotalPages(), 1);
    assertEquals(result.getTotalElements(), 1);
    assertFalse(result.isEmpty());
  }

  @Test
  void findByCustomerId_repositoryFilledAndAmountParamUsed_containsNoResults() {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    repository.saveAll(transactions);
    var params = new TransactionParams();
    params.setAmount(2001L);

    var testedTransaction = transactions.get(0);

    var result =
        repository.findByCustomerId(
            testedTransaction.getSender().getCustomer().getCustomerId(),
            params,
            params.getPageRequest());

    assertEquals(result.getTotalPages(), 0);
    assertEquals(result.getTotalElements(), 0);
    assertTrue(result.isEmpty());
  }

  @Test
  void findByMonthAndYear_correctMonth_containsResults() {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    repository.saveAll(transactions);
    var params = new TransactionParams();
    params.setAmount(2001L);

    var testedTransaction = transactions.get(0);

    var currentYearMonth = YearMonth.now();

    var result =
        repository.findByMonthAndYear(
            testedTransaction.getSender().getAccountId(),
            currentYearMonth.getYear(),
            currentYearMonth.getMonthValue());

    assertEquals(result.size(), 3);
  }

  @Test
  void findByMonthAndYear_incorrectMonth_noResults() {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    repository.saveAll(transactions);
    var params = new TransactionParams();
    params.setAmount(2001L);

    var testedTransaction = transactions.get(0);

    var currentYearMonth = YearMonth.of(1900, Month.DECEMBER);

    var result =
        repository.findByMonthAndYear(
            testedTransaction.getSender().getAccountId(),
            currentYearMonth.getYear(),
            currentYearMonth.getMonthValue());

    assertEquals(result.size(), 0);
  }
}
