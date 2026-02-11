package com.moneytransfer.exception;

/**
 * Thrown when an account ID doesn't exist in the system
 */
public class AccountNotFoundException extends MoneyTransferException {

    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID: " + accountId);
    }
}