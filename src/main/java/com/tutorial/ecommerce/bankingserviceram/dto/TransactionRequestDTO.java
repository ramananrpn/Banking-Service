package com.tutorial.ecommerce.bankingserviceram.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {

  @NotNull(message = "Account ID is required")
  @Positive(message = "Account ID must be a positive number")
  private String accountId;

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
  private BigDecimal amount;

  @Size(max = 255, message = "Description must be 255 characters or less")
  private String description;
}
