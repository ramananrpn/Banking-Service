package com.tutorial.ecommerce.bankingserviceram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long accountId;
    private Long userId;
    private String username;
    private String email;
    private String accountType;
    private double balance;
    private String aadhaar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
