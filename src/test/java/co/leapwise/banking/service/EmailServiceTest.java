package co.leapwise.banking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import co.leapwise.banking.TestUtils;
import co.leapwise.banking.common.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @Mock private JavaMailSender javaMailSender;

  @Mock private Properties properties;

  @InjectMocks private EmailService emailService;

  @Captor private ArgumentCaptor<SimpleMailMessage> captor;

  @Test
  void sendTransactionSuccessConfirmation() {
    var transaction = TestUtils.getTransactions(1000L).get(0);

    emailService.sendTransactionSuccessConfirmation(transaction, 2000L, 3000L);

    verify(javaMailSender, times(2)).send(captor.capture());

    var actualMessages = captor.getAllValues();

    assertEquals(transaction.getSender().getCustomer().getEmail(), Objects.requireNonNull(actualMessages.get(0).getTo())[0]);
    assertEquals("Transaction status", actualMessages.get(0).getSubject());
    assertTrue(Objects.requireNonNull(actualMessages.get(0).getText()).contains("taken from"));
    assertTrue(Objects.requireNonNull(actualMessages.get(0).getText()).contains("2000"));
    assertTrue(Objects.requireNonNull(actualMessages.get(0).getText()).contains("1000"));

    assertEquals(transaction.getReceiver().getCustomer().getEmail(), Objects.requireNonNull(actualMessages.get(1).getTo())[0]);
    assertEquals("Transaction status", actualMessages.get(1).getSubject());
    assertTrue(Objects.requireNonNull(actualMessages.get(1).getText()).contains("added to"));
    assertTrue(Objects.requireNonNull(actualMessages.get(1).getText()).contains("3000"));
    assertTrue(Objects.requireNonNull(actualMessages.get(1).getText()).contains("4000"));
  }

  @Test
  void sendTransactionFailConfirmation() {
    var testMail = "a@a.a";
    emailService.sendTransactionFailConfirmation(testMail);

    verify(javaMailSender, times(1)).send(captor.capture());

    var actualMessage = captor.getValue();

    assertEquals(testMail, Objects.requireNonNull(actualMessage.getTo())[0]);
    assertEquals("Transaction status", actualMessage.getSubject());
    assertTrue(Objects.requireNonNull(actualMessage.getText()).contains("unsuccessfully"));
  }
}
