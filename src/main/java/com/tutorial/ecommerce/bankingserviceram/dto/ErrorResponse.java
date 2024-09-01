package com.tutorial.ecommerce.bankingserviceram.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private String fieldName;
    private int statusCode;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorResponse(int statusCode, String message, String fieldName) {
        this.statusCode = statusCode;
        this.message = message;
        this.fieldName = fieldName;
    }
}
