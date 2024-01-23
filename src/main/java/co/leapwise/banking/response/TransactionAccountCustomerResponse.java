package co.leapwise.banking.response;

import lombok.Data;

@Data
public class TransactionAccountCustomerResponse {
  private Long customerId;
  private String name;
  private String address;
  private String email;
  private String phoneNumber;
}
