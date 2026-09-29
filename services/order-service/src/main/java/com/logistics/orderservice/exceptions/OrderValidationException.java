package com.logistics.orderservice.exceptions;

public class OrderValidationException extends RuntimeException {

    public OrderValidationException(String message) {
        super(message);
    }
}