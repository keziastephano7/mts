package com.moneytransfer.exception;

/**
 * Base exception for all Money Transfer System errors
 */
public class MoneyTransferException extends RuntimeException {

    public MoneyTransferException(String message) {
        super(message);
    }

    public MoneyTransferException(String message, Throwable cause) {
        super(message, cause);
    }
}