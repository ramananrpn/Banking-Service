package com.tutorial.ecommerce.bankingserviceram.service;


import com.tutorial.ecommerce.bankingserviceram.dto.TransactionRequestDTO;
import com.tutorial.ecommerce.bankingserviceram.dto.TransferRequestDTO;
import com.tutorial.ecommerce.bankingserviceram.exception.AccountNotFoundException;
import com.tutorial.ecommerce.bankingserviceram.exception.BadRequestException;
import com.tutorial.ecommerce.bankingserviceram.exception.InsufficientBalanceException;
import com.tutorial.ecommerce.bankingserviceram.model.Account;
import com.tutorial.ecommerce.bankingserviceram.model.Transaction;
import com.tutorial.ecommerce.bankingserviceram.model.TransactionType;
import com.tutorial.ecommerce.bankingserviceram.repository.AccountRepository;
import com.tutorial.ecommerce.bankingserviceram.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.mail.MessagingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              EmailService emailService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.emailService = emailService;
    }

    public List<Transaction> getStatements(Long accountId, String startDate, String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startDateTime = LocalDateTime.parse(startDate + " 00:00:00", formatter);
            endDateTime = LocalDateTime.parse(endDate + " 00:00:00", formatter);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format, use yyyy-MM-dd");
        }

        if (startDateTime.isAfter(endDateTime)) {
            throw new BadRequestException("Start date must be before end date");
        }

        return transactionRepository.findByAccountIdAndTimestampBetween(accountId, startDateTime, endDateTime);
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Transactional
    public Transaction deposit(TransactionRequestDTO transactionRequestDTO) throws AccountNotFoundException {
        String accountId = transactionRequestDTO.getAccountId();
        BigDecimal amount = transactionRequestDTO.getAmount();
        String description = transactionRequestDTO.getDescription();

        Account account = validateAndRetrieveAccount(accountId);
        updateAccountBalance(account, amount);

        Transaction transaction = createTransaction(account, amount, description, TransactionType.DEPOSIT);
        transactionRepository.save(transaction);

        sendTransactionNotification(account, transaction);
        return transaction;
    }

    @Transactional
    public Transaction withdraw(TransactionRequestDTO transactionRequestDTO) throws AccountNotFoundException, MailException {
        String accountId = transactionRequestDTO.getAccountId();
        BigDecimal amount = transactionRequestDTO.getAmount();
        String description = transactionRequestDTO.getDescription();
        Account account = validateAndRetrieveAccount(accountId);
        validateSufficientBalance(account, amount);

        updateAccountBalance(account, amount.negate());

        Transaction transaction = createTransaction(account, amount, description, TransactionType.WITHDRAWAL);
        transactionRepository.save(transaction);

        sendTransactionNotification(account, transaction);
        return transaction;
    }

    @Transactional
    public Transaction transfer(TransferRequestDTO transferRequestDTO) throws AccountNotFoundException {
        String fromAccountId = transferRequestDTO.getAccountId();
        String toAccountId = transferRequestDTO.getToAccountId();
        BigDecimal amount = transferRequestDTO.getAmount();
        String description = transferRequestDTO.getDescription();
        Account fromAccount = validateAndRetrieveAccount(fromAccountId);
        Account toAccount = validateAndRetrieveAccount(toAccountId);

        validateSufficientBalance(fromAccount, amount);
        updateAccountBalance(fromAccount, amount.negate());
        updateAccountBalance(toAccount, amount);

        // Create a transaction for the sender account
        Transaction senderTransaction = createTransaction(fromAccount, amount, description, TransactionType.TRANSFER);
        transactionRepository.save(senderTransaction);
        sendTransactionNotification(fromAccount, senderTransaction);

        // Create a transaction for the receiver account
        Transaction receiverTransaction = createTransaction(toAccount, amount, description, TransactionType.DEPOSIT);
        transactionRepository.save(receiverTransaction);
        sendTransactionNotification(toAccount, receiverTransaction);

        return senderTransaction;
    }

    private Account validateAndRetrieveAccount(String accountId) throws AccountNotFoundException {
        return accountRepository.findById(Long.valueOf(accountId))
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    private void updateAccountBalance(Account account, BigDecimal amount) {
        BigDecimal newBalance = BigDecimal.valueOf(account.getBalance()).add(amount);
        account.setBalance(newBalance.doubleValue());
        accountRepository.save(account);
    }

    private void validateSufficientBalance(Account account, BigDecimal amount) {
        BigDecimal balance = BigDecimal.valueOf(account.getBalance());
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    private Transaction createTransaction(Account account, BigDecimal amount, String description, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAccount(account);
        transaction.setAmount(amount.doubleValue());
        transaction.setDescription(description);
        transaction.setTransactionType(transactionType);
        transaction.setTimestamp(LocalDateTime.now());

        // Save the transaction to the database
        transactionRepository.save(transaction);

        return transaction;
    }

    private void sendTransactionNotification(Account account, Transaction transaction) {
        try {
            emailService.sendTransactionNotification(account, transaction);
        } catch (MessagingException | MailSendException e) {
            log.error("Failed to send email notification for account ID: {}", account.getId(), e);
        }
    }

    public void createTransaction(Transaction initialTransaction) {
        transactionRepository.save(initialTransaction);
    }
}
