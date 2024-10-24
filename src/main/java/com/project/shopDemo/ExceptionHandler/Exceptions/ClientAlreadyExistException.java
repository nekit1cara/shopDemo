package com.project.shopDemo.ExceptionHandler.Exceptions;

public class ClientAlreadyExistException extends RuntimeException {
    public ClientAlreadyExistException(String message) {
        super(message);
    }
}
