package com.example.shopbackend.demo.common;

import java.util.Map;

public record ApiError(
        String error,
        String message,
        Map<String, String> fieldErrors) {

}
