package co.leapwise.banking.manager;

import co.leapwise.banking.model.Account;
import co.leapwise.banking.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountManager {
  private final AccountRepository repository;

  public Account getAccount(Long accountId) {
    return repository
        .findById(accountId)
        .orElseThrow(() -> new EntityNotFoundException("Account not found. ID: " + accountId));
  }

  public List<Account> getAll() {
    var ret = new ArrayList<Account>();
    repository.findAll().forEach(ret::add);
    return ret;
  }

  public void insertAccount(Account account) {
    repository.save(account);
  }

  public void insertAccounts(List<Account> accounts) {
    repository.saveAll(accounts);
  }
}
