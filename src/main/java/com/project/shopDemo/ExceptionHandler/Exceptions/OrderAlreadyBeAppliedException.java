package com.project.shopDemo.ExceptionHandler.Exceptions;

public class OrderAlreadyBeAppliedException extends RuntimeException {
    public OrderAlreadyBeAppliedException(String message) {
        super(message);
    }
}
