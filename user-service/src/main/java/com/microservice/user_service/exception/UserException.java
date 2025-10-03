package com.microservice.user_service.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}