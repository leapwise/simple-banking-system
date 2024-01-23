package co.leapwise.banking.manager;

import co.leapwise.banking.model.Currency;
import co.leapwise.banking.repository.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyManager {
  private final CurrencyRepository repository;

  public Currency getCurrency(Long currencyId) {
    return repository
        .findById(currencyId)
        .orElseThrow(() -> new EntityNotFoundException("Currency not found. ID: " + currencyId));
  }

  public void insertCurrency(Currency currency) {
    repository.save(currency);
  }

  public void insertCurrencies(List<Currency> currencies) {
    repository.saveAll(currencies);
  }
}
