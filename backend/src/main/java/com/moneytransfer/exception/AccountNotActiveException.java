package com.moneytransfer.exception;

/**
 * Thrown when trying to perform operations on a LOCKED or CLOSED account
 */
public class AccountNotActiveException extends MoneyTransferException {

    public AccountNotActiveException(String message) {
        super(message);
    }
}