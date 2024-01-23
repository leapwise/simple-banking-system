package co.leapwise.banking.controller;

import co.leapwise.banking.response.CustomerResponse;
import co.leapwise.banking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerService service;

  @GetMapping("/{customerId}")
  public CustomerResponse getCustomer(@PathVariable Long customerId) {
    return service.getCustomerResponse(customerId);
  }
}
