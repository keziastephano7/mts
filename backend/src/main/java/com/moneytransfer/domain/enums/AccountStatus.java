package com.moneytransfer.domain.enums;

/**
 * Represents the status of a bank account
 */
public enum AccountStatus {
    /**
     * Account is active and can perform transactions
     */
    ACTIVE,

    /**
     * Account is temporarily locked (e.g., suspicious activity)
     */
    LOCKED,

    /**
     * Account is permanently closed
     */
    CLOSED
}