package com.tutorial.ecommerce.bankingserviceram.service;


import com.tutorial.ecommerce.bankingserviceram.dto.CreateAccountRequest;
import com.tutorial.ecommerce.bankingserviceram.dto.UpdateAccountRequest;
import com.tutorial.ecommerce.bankingserviceram.exception.BadRequestException;
import com.tutorial.ecommerce.bankingserviceram.model.Account;
import com.tutorial.ecommerce.bankingserviceram.model.Transaction;
import com.tutorial.ecommerce.bankingserviceram.model.TransactionType;
import com.tutorial.ecommerce.bankingserviceram.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public Account createAccount(CreateAccountRequest createAccountRequest) {
        // Validate initial deposit amount
        if (createAccountRequest.getInitialDeposit() < 0) {
            throw new BadRequestException("Initial deposit amount cannot be negative");
        }

        // Validate Aadhaar number format and uniqueness
        if (!createAccountRequest.getAadhaar().matches("\\d{4}-\\d{4}-\\d{4}")) {
            throw new BadRequestException("Invalid Aadhaar number format");
        }

        Optional<Account> existingAccount = accountRepository.findByAadhaar(createAccountRequest.getAadhaar());
        if (existingAccount.isPresent()) {
            throw new BadRequestException("Aadhaar number already exists");
        }

        // Create and save the account
        Account account = Account.builder()
                .accountType(createAccountRequest.getAccountType())
                .aadhaar(createAccountRequest.getAadhaar())
                .balance(createAccountRequest.getInitialDeposit())
                .username(createAccountRequest.getUsername())
                .email(createAccountRequest.getEmail())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        account = accountRepository.save(account);

        Transaction initialTransaction = Transaction.builder()
                .account(account)
                .transactionId(UUID.randomUUID().toString())
                .description("Initial deposit")
                .amount(createAccountRequest.getInitialDeposit())
                .transactionType(TransactionType.DEPOSIT)
                .timestamp(LocalDateTime.now())
                .build();
        transactionService.createTransaction(initialTransaction);

        return account;
    }

    public Account updateAccount(Long id, UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Account not found"));


        account.setAccountType(updateAccountRequest.getAccountType());
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Account not found"));

        accountRepository.delete(account);
    }

    public double getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Account not found"));
        return account.getBalance();
    }
}
