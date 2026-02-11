package com.moneytransfer.exception;

/**
 * Thrown when same idempotency key is used twice (duplicate transfer attempt)
 */
public class DuplicateTransferException extends MoneyTransferException {

    public DuplicateTransferException(String idempotencyKey) {
        super("Duplicate transfer detected with idempotency key: " + idempotencyKey);
    }
}