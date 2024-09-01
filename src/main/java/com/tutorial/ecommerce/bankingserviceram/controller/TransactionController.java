package com.tutorial.ecommerce.bankingserviceram.controller;


import com.tutorial.ecommerce.bankingserviceram.dto.TransactionRequestDTO;
import com.tutorial.ecommerce.bankingserviceram.dto.TransferRequestDTO;
import com.tutorial.ecommerce.bankingserviceram.model.Transaction;
import com.tutorial.ecommerce.bankingserviceram.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.security.auth.login.AccountNotFoundException;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/deposit")
  public ResponseEntity<Transaction> deposit(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO)
          throws AccountNotFoundException {
    Transaction transaction = transactionService.deposit(transactionRequestDTO);
    return new ResponseEntity<>(transaction, HttpStatus.OK);
  }

  @PostMapping("/withdraw")
  public ResponseEntity<Transaction> withdraw(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO)
          throws AccountNotFoundException, MessagingException {
    Transaction transaction = transactionService.withdraw(transactionRequestDTO);
    return new ResponseEntity<>(transaction, HttpStatus.OK);
  }

  @PostMapping("/transfer")
  public ResponseEntity<Transaction> transfer(@RequestBody @Valid TransferRequestDTO transferRequestDTO)
          throws AccountNotFoundException {
    Transaction transaction = transactionService.transfer(transferRequestDTO);
    return new ResponseEntity<>(transaction, HttpStatus.OK);
  }
}
