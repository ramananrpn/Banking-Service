package com.tutorial.ecommerce.bankingserviceram.dto.response;


import com.tutorial.ecommerce.bankingserviceram.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class StatementResponse {

    private Long accountId;
    private List<Transaction> statements;
    private String startDate;
    private String endDate;
}
