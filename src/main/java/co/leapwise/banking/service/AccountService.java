package co.leapwise.banking.service;

import co.leapwise.banking.manager.MTransactionManager;
import co.leapwise.banking.model.Transaction;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final MTransactionManager transactionManager;

  public Long calculatePastMonthTurnover(Long accountId) {
    var pastMonth = YearMonth.now().minus(1, ChronoUnit.MONTHS);
    var transactions = transactionManager.getByAccountIdAndYearMonth(accountId, pastMonth);

    return transactions.stream().mapToLong(Transaction::getAmount).sum();
  }
}
