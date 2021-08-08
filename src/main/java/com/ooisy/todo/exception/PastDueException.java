package com.ooisy.todo.exception;

public class PastDueException extends RuntimeException {

    public PastDueException(String message) {
        super(message);
    }
}
