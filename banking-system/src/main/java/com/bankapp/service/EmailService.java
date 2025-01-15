package com.bankapp.service;

import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendTransferNotification(User sourceUser, User destinationUser, 
                                       Transaction transaction, BigDecimal newBalance) {
        // Send to source user (money sent)
        sendTransferSentEmail(sourceUser, transaction, newBalance);
        
        // Send to destination user (money received)
        sendTransferReceivedEmail(destinationUser, transaction);
    }

    private void sendTransferSentEmail(User user, Transaction transaction, BigDecimal newBalance) {
        Context context = new Context();
        context.setVariable("name", user.getFirstName());
        context.setVariable("amount", transaction.getAmount());
        context.setVariable("destinationAccount", transaction.getDestinationAccount().getAccountNumber());
        context.setVariable("reference", transaction.getReferenceNumber());
        context.setVariable("newBalance", newBalance);
        
        String emailContent = templateEngine.process("transfer-sent", context);
        
        try {
            sendHtmlEmail(
                user.getEmail(),
                "Money Transfer Sent - " + transaction.getReferenceNumber(),
                emailContent
            );
        } catch (MessagingException e) {
            // Log the error but don't throw it to prevent transaction rollback
            System.err.println("Failed to send transfer sent email: " + e.getMessage());
        }
    }

    private void sendTransferReceivedEmail(User user, Transaction transaction) {
        Context context = new Context();
        context.setVariable("name", user.getFirstName());
        context.setVariable("amount", transaction.getAmount());
        context.setVariable("sourceAccount", transaction.getAccount().getAccountNumber());
        context.setVariable("reference", transaction.getReferenceNumber());
        
        String emailContent = templateEngine.process("transfer-received", context);
        
        try {
            sendHtmlEmail(
                user.getEmail(),
                "Money Transfer Received - " + transaction.getReferenceNumber(),
                emailContent
            );
        } catch (MessagingException e) {
            // Log the error but don't throw it to prevent transaction rollback
            System.err.println("Failed to send transfer received email: " + e.getMessage());
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        emailSender.send(message);
    }
}