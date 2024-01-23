package co.leapwise.banking.service;

import co.leapwise.banking.manager.CustomerManager;
import co.leapwise.banking.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerManager manager;
  private final ModelMapper modelMapper;

  public CustomerResponse getCustomerResponse(Long customerId) {
    return modelMapper.map(manager.getCustomer(customerId), CustomerResponse.class);
  }
}
