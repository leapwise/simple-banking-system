package co.leapwise.banking.manager;

import co.leapwise.banking.model.Customer;
import co.leapwise.banking.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerManager {
  private final CustomerRepository repository;

  public Customer getCustomer(Long customerId) {
    return repository
        .findById(customerId)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found. ID: " + customerId));
  }

  public void insertCustomer(Customer customer) {
    repository.save(customer);
  }
}
