package co.leapwise.banking.service;

public interface CurrencyConverter {
  Long convert(Long inputAmount, Long inputCurrencyId, Long outputCurrencyId);
}
