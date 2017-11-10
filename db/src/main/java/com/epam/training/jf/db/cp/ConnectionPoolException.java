package com.epam.training.jf.db.cp;

public class ConnectionPoolException extends RuntimeException {
    public ConnectionPoolException(String message, Throwable e) {
        super(message, e);
    }

    public ConnectionPoolException(String message) {
        super(message);
    }
}
