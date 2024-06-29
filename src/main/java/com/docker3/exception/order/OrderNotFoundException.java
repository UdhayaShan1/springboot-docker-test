package com.docker3.exception.order;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String error) {
        super(error);
    }
}
