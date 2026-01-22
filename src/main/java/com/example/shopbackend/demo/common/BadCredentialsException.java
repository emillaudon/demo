package com.example.shopbackend.demo.common;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
        super("Bad credentials");
    }
}
