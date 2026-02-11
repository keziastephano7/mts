package com.moneytransfer.exception;

/**
 * Thrown when account doesn't have enough money for transfer
 */
public class InsufficientBalanceException extends MoneyTransferException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}