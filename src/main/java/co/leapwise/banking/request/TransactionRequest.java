package co.leapwise.banking.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequest {
    private Long senderId;
    private Long receiverId;
    private Long amount;
    private Long currencyId;
    private String message;
}
