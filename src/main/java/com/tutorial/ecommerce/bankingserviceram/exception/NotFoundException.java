package com.tutorial.ecommerce.bankingserviceram.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
