package co.leapwise.banking.manager;

import co.leapwise.banking.model.Transaction;
import co.leapwise.banking.repository.TransactionRepository;
import co.leapwise.banking.request.params.TransactionParams;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MTransactionManager {
  private final TransactionRepository repository;

  public void insertTransaction(Transaction transaction) {
    repository.save(transaction);
  }

  public void insertTransactions(List<Transaction> transactions) {
    repository.saveAll(transactions);
  }

  public Page<Transaction> getByCustomerId(Long customerId, TransactionParams params) {
    return repository.findByCustomerId(customerId, params, params.getPageRequest());
  }

  public List<Transaction> getByAccountIdAndYearMonth(Long accountId, YearMonth yearMonth) {
    return repository.findByMonthAndYear(accountId, yearMonth.getYear(), yearMonth.getMonthValue());
  }
}
