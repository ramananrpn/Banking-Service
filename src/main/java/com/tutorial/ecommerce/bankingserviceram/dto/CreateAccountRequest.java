package com.tutorial.ecommerce.bankingserviceram.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotEmpty
    private String username;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String accountType;

    @Positive
    private double initialDeposit;

    @NotEmpty
    private String aadhaar;
}
