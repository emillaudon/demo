package com.example.shopbackend.demo.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {
        
        @ExceptionHandler(MaxUploadSizeException.class)
        @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE) 
        public ApiError handleUploadSizeAboveMax(MaxUploadSizeException ex) {
                return new ApiError(
                        "UPLOAD_SIZE_ABOVE_MAX",
                        ex.getMessage(),
                        Map.of(
                                "size", ex.getActualBytes(),
                                "max", ex.getMaxBytes()
                        )
                );
        }

        @ExceptionHandler(InvalidImageTypeException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiError handleImageWrongType(InvalidImageTypeException ex) {
                return new ApiError(
                        "IMAGE_WRONG_TYPE",
                        ex.getMessage(),
                        Map.of(
                                "type", ex.getContentType(),
                                "allowed", ex.getAllowed()
                        )
                );
        }

        @ExceptionHandler(ImageTooLargeException.class)
        @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
        public ApiError handleImageTooLarge(ImageTooLargeException ex) {
                return new ApiError(
                        "IMAGE_TOO_LARGE",
                        ex.getMessage(),
                        Map.of(
                                "size", ex.getActualBytes(),
                                "max", ex.getMaxBytes()
                        )
                );
        }

        @ExceptionHandler(InvalidPriceRangeException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiError handleInvalidPriceRange(InvalidPriceRangeException ex) {
                return new ApiError(
                                "INVALID_PRICE_RANGE",
                                ex.getMessage(),
                                Map.of(
                                                "from", ex.getFrom(),
                                                "to", ex.getTo()));
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiError handleMissingParam(MissingServletRequestParameterException ex) {
                return new ApiError(
                                "MISSING_PARAMETER",
                                "Required query parameter is missing",
                                Map.of(
                                                "parameter", ex.getParameterName(),
                                                "expectedType", ex.getParameterType()));
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiError handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
                return new ApiError(
                                "INVALID_PARAMETER",
                                "Invalid value for parameter '" + ex.getName() + "'",
                                Map.of(
                                                "parameter", ex.getName(),
                                                "value", String.valueOf(ex.getValue()),
                                                "expectedType", ex.getRequiredType().getSimpleName()));
        }

        @ExceptionHandler(InvalidDateRangeException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiError handleInvalidDateRange(InvalidDateRangeException ex) {
                return new ApiError(
                                "INVALID_DATE_RANGE",
                                ex.getMessage(),
                                Map.of("from", ex.getFrom(),
                                                "to", ex.getTo()));
        }

        @ExceptionHandler(InvalidStatusTransitionException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ApiError handleInvalidStatusTransition(InvalidStatusTransitionException ex) {
                return new ApiError(
                                "INVALID_STATUS_TRANSITION",
                                ex.getMessage(),
                                Map.of("status", ex.getStatus(),
                                                "allowed", ex.getAllowed()));
        }

        @ExceptionHandler(InvalidStatusException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiError handleInvalidStatus(InvalidStatusException ex) {
                return new ApiError(
                                "INVALID_STATUS",
                                ex.getMessage(),
                                Map.of(
                                                "Status", ex.getStatus(),
                                                "allowed", ex.getAllowed()));
        }

        @ExceptionHandler(OutOfStockException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ApiError handleOutOfStock(OutOfStockException ex) {
                return new ApiError(
                                "OUT_OF_STOCK",
                                ex.getMessage(),
                                Map.of(
                                                "productId", ex.getProductId(),
                                                "requested", ex.getRequested(),
                                                "available", ex.getAvailable()));
        }

        @ExceptionHandler(NotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ApiError handleNotFound(NotFoundException ex) {
                return new ApiError(
                                "NOT_FOUND",
                                ex.getMessage(),
                                Map.of());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiError handleValidation(MethodArgumentNotValidException ex) {
                Map<String, Object> fieldErrors = new LinkedHashMap<>();

                ex.getBindingResult().getFieldErrors().forEach(err -> {
                        fieldErrors.putIfAbsent(err.getField(), err.getDefaultMessage());
                });

                return new ApiError(
                                "VALIDATION_ERROR",
                                "Request validation failed",
                                fieldErrors);
        }

}
