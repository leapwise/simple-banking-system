package co.leapwise.banking.request.params;

import co.leapwise.banking.common.AccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionParams extends PageableParams {
  private Long transactionId;
  private Long amount;
  private String currencyName;

  private Long senderAccountId;
  private String senderAccountNumber;
  private AccountType senderAccountType;
  private Long senderCustomerId;
  private String senderName;
  private String senderAddress;
  private String senderEmail;
  private String senderPhoneNumber;

  private Long receiverAccountId;
  private String receiverAccountNumber;
  private AccountType receiverAccountType;
  private Long receiverCustomerId;
  private String receiverName;
  private String receiverAddress;
  private String receiverEmail;
  private String receiverPhoneNumber;

  // TODO add other filter properties if needed
}
