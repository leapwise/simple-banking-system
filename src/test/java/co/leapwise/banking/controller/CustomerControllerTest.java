package co.leapwise.banking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.leapwise.banking.TestUtils;
import co.leapwise.banking.manager.MTransactionManager;
import co.leapwise.banking.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
  @Autowired MockMvc mockMvc;

  @Autowired MTransactionManager transactionManager;

  @MockBean EmailService emailService;

  @Test
  @DirtiesContext
  void getCustomer_givenId_returnsValidCustomer() throws Exception {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    transactionManager.insertTransactions(transactions);

    var customer = transactions.get(0).getSender().getCustomer();

    mockMvc
        .perform(get("/customer/{customerId}", customer.getCustomerId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customerId").value(customer.getCustomerId()))
        .andExpect(jsonPath("$.name").value(customer.getName()))
        .andExpect(jsonPath("$.address").value(customer.getAddress()))
        .andExpect(jsonPath("$.email").value(customer.getEmail()))
        .andExpect(jsonPath("$.phoneNumber").value(customer.getPhoneNumber()))
        .andExpect(
            jsonPath("$.accounts[0].accountNumber")
                .value(customer.getAccounts().get(0).getAccountNumber()))
        .andExpect(
            jsonPath("$.accounts[0].balance").value(customer.getAccounts().get(0).getBalance()))
        .andExpect(
            jsonPath("$.accounts[1].accountNumber")
                .value(customer.getAccounts().get(0).getAccountNumber()))
        .andExpect(
            jsonPath("$.accounts[1].balance").value(customer.getAccounts().get(1).getBalance()));
    ;
  }

  @Test
  @DirtiesContext
  void getCustomer_givenNonExistentId_exceptionResponse() throws Exception {
    var transactions = TestUtils.getTransactions(1000L, 2000L, 3000L);
    transactionManager.insertTransactions(transactions);

    var customerId = Long.MAX_VALUE;

    mockMvc
        .perform(get("/customer/{customerId}", customerId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Entity not found"));
  }
}
