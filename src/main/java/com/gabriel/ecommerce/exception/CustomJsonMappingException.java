package com.gabriel.ecommerce.exception;

public class CustomJsonMappingException extends RuntimeException {
    public CustomJsonMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
