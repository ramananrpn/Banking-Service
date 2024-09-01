package com.tutorial.ecommerce.bankingserviceram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceResponse {
    private Long accountId;
    private double balance;
}
