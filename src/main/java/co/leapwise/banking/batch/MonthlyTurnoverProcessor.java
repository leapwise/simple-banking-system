package co.leapwise.banking.batch;

import co.leapwise.banking.model.Account;
import co.leapwise.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyTurnoverProcessor implements ItemProcessor<Account, Account> {
  private final AccountService accountService;

  @Override
  public Account process(@NonNull Account account) {
    var pastMonthTurnover = accountService.calculatePastMonthTurnover(account.getAccountId());
    account.setPastMonthTurnover(pastMonthTurnover);
    return account;
  }
}
