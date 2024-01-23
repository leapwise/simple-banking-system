package co.leapwise.banking.dev;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerializedTransaction {
  private Long senderId;
  private Long receiverId;
  private Long amount;
  private Long currencyId;
  private String message;
  private LocalDateTime timeStamp;
}
