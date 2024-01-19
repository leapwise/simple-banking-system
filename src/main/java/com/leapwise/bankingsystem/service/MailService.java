package com.leapwise.bankingsystem.service;

import com.leapwise.bankingsystem.config.properties.EmailProperties;
import com.leapwise.bankingsystem.domain.Account;
import com.leapwise.bankingsystem.domain.Transaction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailService {

    JavaMailSender javaMailSender;
    EmailProperties emailProperties;

    public MimeMessage buildMessage(Transaction transaction, Account account, boolean isSuccess, BigDecimal oldBalance) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, UTF_8.name());
            mimeMessageHelper.setFrom(emailProperties.getFrom());
            mimeMessageHelper.setTo(account.getCustomer().getEmail());

            mimeMessageHelper.setSubject("Transaction Notification");

            String emailBody = createTransactionEmailBody(transaction, account, isSuccess, oldBalance);
            mimeMessage.setText(emailBody);
        } catch (MessagingException e) {
            throw new RuntimeException("Error while preparing email content.");
        }

        return mimeMessage;
    }

    public void send(MimeMessage mimeMessage) {
        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new RuntimeException("Email was not sent correctly.");
        }
    }
    private String createTransactionEmailBody(Transaction transaction, Account account, boolean isSuccess, BigDecimal oldBalance) {
        String status = isSuccess ? "successfully" : "unsuccessfully";
        String action = isSender(transaction, account) ? "taken" : "added";

        return String.format("Hello!\n\n" +
                "The transaction with ID: %s has been processed %s, and the balance: %s has been %s from your account.\n\n" +
                "Old balance: %s\n" +
                "New balance: %s\n\n" +
                "Regards,\n" +
                "Your XYZ bank", transaction.getId(), status, transaction.getAmount(), action, oldBalance, account.getBalance());
    }

    private boolean isSender(Transaction transaction, Account account) {
        return transaction.getSenderAccount().equals(account);
    }
}
