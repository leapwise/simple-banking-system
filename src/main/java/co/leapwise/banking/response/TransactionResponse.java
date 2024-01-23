package co.leapwise.banking.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransactionResponse {
  private Long transactionId;
  private TransactionAccountResponse sender;
  private TransactionAccountResponse receiver;
  private Long amount;
  private String currencyName;
  private String message;
  private LocalDateTime timeStamp;
}
