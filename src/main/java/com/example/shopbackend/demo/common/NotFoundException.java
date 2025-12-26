package com.example.shopbackend.demo.common;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, Long id) {
        super(resource + " with id " + id + " not found");
    }

}
