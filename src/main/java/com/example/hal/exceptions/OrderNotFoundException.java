package com.example.hal.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(final String msg) {
        super(msg);
    }
}
