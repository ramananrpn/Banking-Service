package com.tutorial.ecommerce.bankingserviceram.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}