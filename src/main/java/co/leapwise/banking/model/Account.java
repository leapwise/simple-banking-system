package co.leapwise.banking.model;

import co.leapwise.banking.common.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountId;

  @NotBlank(message = "Account number is required.")
  private String accountNumber;

  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @NotNull(message = "Balance must not be null.")
  private Long balance;

  private Long pastMonthTurnover;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "customerId")
  private Customer customer;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Transaction> senderTransactions;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<Transaction> receiverTransactions;
}
