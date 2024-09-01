package com.tutorial.ecommerce.bankingserviceram.service;


import com.tutorial.ecommerce.bankingserviceram.exception.AccountNotFoundException;
import com.tutorial.ecommerce.bankingserviceram.model.Account;
import com.tutorial.ecommerce.bankingserviceram.model.Transaction;
import com.tutorial.ecommerce.bankingserviceram.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


@Service
public class EmailService {

  @Value("${spring.mail.from}")
  private String fromEmail;

  private AccountRepository accountRepository;

  private final JavaMailSender mailSender;


  public EmailService(AccountRepository accountRepository,
                      JavaMailSender mailSender) {
    this.accountRepository = accountRepository;
    this.mailSender = mailSender;
  }


  public void sendTransactionNotification(Account account, Transaction transaction) throws MessagingException {
    MimeMessageHelper message = new MimeMessageHelper(mailSender.createMimeMessage(), true);
    message.setFrom(fromEmail);
    message.setTo(account.getEmail());
    message.setSubject("Transaction Alert: " + transaction.getTransactionId());
    message.setText(buildEmailBody(transaction, account));

    mailSender.send(message.getMimeMessage());
  }

  private String buildEmailBody(Transaction transaction, Account account) {
    return "Dear Customer,\n\n" +
            "A transaction has been made on your account.\n\n" +
            "Transaction Details:\n" +
            "Transaction ID: " + transaction.getTransactionId() + "\n" +
            "Account ID: " + transaction.getAccount().getId() + "\n" +
            "Amount: " + transaction.getAmount() + "\n" +
            "Description: " + transaction.getDescription() + "\n" +
            "Timestamp: " + transaction.getTimestamp() + "\n\n" +
            "Current Balance: " + account.getBalance() + "\n\n" +
            "Thank you for banking with us.\n\n";
  }

  private String getEmailForAccount(String accountId) {
   return accountRepository.findById(Long.valueOf(accountId))
            .map(Account::getEmail)
            .orElseThrow(() -> new AccountNotFoundException("Account not found"));
  }
}
