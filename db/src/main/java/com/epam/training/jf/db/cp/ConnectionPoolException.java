package com.epam.training.jf.db.cp;

public class ConnectionPoolException extends RuntimeException {
    public ConnectionPoolException(String message, Exception e) {
        super(message, e);
    }
}
