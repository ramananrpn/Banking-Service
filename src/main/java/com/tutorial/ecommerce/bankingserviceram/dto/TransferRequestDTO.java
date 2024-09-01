package com.tutorial.ecommerce.bankingserviceram.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;



@Data
public class TransferRequestDTO extends TransactionRequestDTO{
  @NotNull(message = "Recipient Account ID cannot be null")
  @Positive(message = "Recipient Account ID must be a positive number")
  private String toAccountId;
}
