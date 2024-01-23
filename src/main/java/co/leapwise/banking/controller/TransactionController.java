package co.leapwise.banking.controller;

import co.leapwise.banking.request.TransactionRequest;
import co.leapwise.banking.request.params.TransactionParams;
import co.leapwise.banking.response.PageableResponse;
import co.leapwise.banking.response.TransactionResponse;
import co.leapwise.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
  private final TransactionService service;

  @PostMapping
  public Long postTransaction(@RequestBody TransactionRequest request) {
    return service.insertTransaction(request);
  }

  @GetMapping("/history/{customerId}")
  public PageableResponse<TransactionResponse> getTransactionsByCustomer(
      @PathVariable Long customerId, TransactionParams params) {
    return service.getTransactionsByCustomer(customerId, params);
  }
}
