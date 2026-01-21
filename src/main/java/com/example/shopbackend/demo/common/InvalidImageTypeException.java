package com.example.shopbackend.demo.common;

import java.util.List;

public class InvalidImageTypeException extends RuntimeException {
    private String contentType;
    private List<String> allowed;

        public InvalidImageTypeException(String contentType, List<String> allowed) {
            super("Invalid image content type: " + contentType + ". Allowed types: " + allowed);
            this.contentType = contentType;
            this.allowed = allowed;

    }

    public String getContentType() {
        return contentType;
    }

    public List<String> getAllowed() {
        return allowed;
    }
}
