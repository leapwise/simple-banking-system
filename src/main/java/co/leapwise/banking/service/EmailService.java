package co.leapwise.banking.service;

import co.leapwise.banking.common.Properties;
import co.leapwise.banking.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender javaMailSender;
  private final Properties properties;

  private static final String MSG_FAIL = "The transaction with has been processed unsuccessfully.";

  private static final String MSG_SUCCESS =
      """
The transaction with ID: %d has been processed successfully,
and the amount: %d has been %s your account.

Old balance: %d
New balance: %d""";

  private static final String SUBJECT = "Transaction status";

  public void sendTransactionSuccessConfirmation(
      Transaction transaction, Long senderOldBalance, Long receiverOldBalance) {
    sendEmail(
        transaction.getSender().getCustomer().getEmail(),
        SUBJECT,
        MSG_SUCCESS.formatted(
            transaction.getTransactionId(),
            transaction.getAmount(),
            "taken from",
            senderOldBalance,
            senderOldBalance - transaction.getAmount()));
    sendEmail(
        transaction.getReceiver().getCustomer().getEmail(),
        SUBJECT,
        MSG_SUCCESS.formatted(
            transaction.getTransactionId(),
            transaction.getAmount(),
            "added to",
            receiverOldBalance,
            receiverOldBalance + transaction.getAmount()));
  }

  public void sendTransactionFailConfirmation(String senderEmail) {
    sendEmail(senderEmail, SUBJECT, MSG_FAIL);
  }

  private void sendEmail(String recipient, String subject, String body) {
    var mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(properties.emailSenderAddress());
    mailMessage.setTo(recipient);
    mailMessage.setText(body);
    mailMessage.setSubject(subject);

    try {
      javaMailSender.send(mailMessage);
    } catch (Exception e) {
      throw new RuntimeException("Error sending email", e);
    }
  }
}
