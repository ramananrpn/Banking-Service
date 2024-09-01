package com.tutorial.ecommerce.bankingserviceram.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;



@Data
public class UpdateAccountRequest {

    @NotEmpty
    private String accountType;
}
