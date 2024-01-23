package co.leapwise.banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long transactionId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "senderId")
  private Account sender;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "receiverId")
  private Account receiver;

  @NotNull(message = "Amount must not be null.")
  @Positive(message = "Amount must be a positive number.")
  private Long amount;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "currencyId")
  private Currency currency;

  private String message;

  private LocalDateTime timeStamp;

  @PrePersist
  public void prePersist() {
    if (timeStamp == null) {
      timeStamp = LocalDateTime.now();
    }
  }
}
