package com.example.hal.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(final String msg) {
        super(msg);
    }
}
