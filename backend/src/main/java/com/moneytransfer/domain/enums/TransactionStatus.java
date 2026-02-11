package com.moneytransfer.domain.enums;

/**
 * Represents the outcome of a money transfer transaction
 */
public enum TransactionStatus {
    /**
     * Transaction completed successfully
     */
    SUCCESS,

    /**
     * Transaction failed (e.g., insufficient funds)
     */
    FAILED
}