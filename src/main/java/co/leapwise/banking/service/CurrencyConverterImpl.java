package co.leapwise.banking.service;

import org.springframework.stereotype.Service;

@Service
public class CurrencyConverterImpl implements CurrencyConverter {

  @Override
  public Long convert(Long inputAmount, Long inputCurrencyId, Long outputCurrencyId) {
    // TODO implement currency converter
    return inputAmount;
  }
}
