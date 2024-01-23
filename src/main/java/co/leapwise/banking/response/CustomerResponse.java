package co.leapwise.banking.response;

import java.util.List;
import lombok.Data;

@Data
public class CustomerResponse {
  private Long customerId;
  private String name;
  private String address;
  private String email;
  private String phoneNumber;
  private List<CustomerAccountResponse> accounts;
}
