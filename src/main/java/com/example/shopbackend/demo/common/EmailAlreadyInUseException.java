package com.example.shopbackend.demo.common;

public class EmailAlreadyInUseException extends RuntimeException {
    private final String email;

    public EmailAlreadyInUseException(String email) {
        super("Email already in use");
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
