package co.leapwise.banking.response;

import co.leapwise.banking.common.AccountType;
import lombok.Data;

@Data
public class CustomerAccountResponse {
  private Long accountId;
  private String accountNumber;
  private AccountType accountType;
  private Long balance;
  private Long pastMonthTurnover;
}
