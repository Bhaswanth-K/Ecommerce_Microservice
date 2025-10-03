package com.microservice.api_gateway.exception;

public class ApiGatewayException extends RuntimeException {
    public ApiGatewayException(String message) {
        super(message);
    }
}