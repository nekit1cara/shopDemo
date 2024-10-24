package com.project.shopDemo.ExceptionHandler.Exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
       super(message);
    }
}
