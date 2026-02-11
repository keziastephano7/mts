package com.moneytransfer.domain.entity;

import com.moneytransfer.domain.enums.AccountStatus;
import com.moneytransfer.exception.AccountNotActiveException;
import com.moneytransfer.exception.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a bank account in the money transfer system
 * This is the core domain entity that holds account information and balance
 */
@Data  // Lombok: Auto-generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Lombok: Generates no-argument constructor
@AllArgsConstructor  // Lombok: Generates constructor with all fields
public class Account {

    private Long id;
    private String holderName;
    private BigDecimal balance;
    private AccountStatus status;
    private Integer version;
    private LocalDateTime lastUpdated;

    /**
     * Deduct money from the account (withdrawal)
     *
     * @param amount Amount to deduct
     * @throws AccountNotActiveException if account is not ACTIVE
     * @throws InsufficientBalanceException if balance is insufficient
     */
    public void debit(BigDecimal amount) {
        // Business Rule: Account must be active
        if (!isActive()) {
            throw new AccountNotActiveException("Cannot debit from account " + id + ". Account status: " + status);
        }

        // Business Rule: Must have sufficient balance
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + balance + ", Required: " + amount);
        }

        // Deduct the amount
        this.balance = this.balance.subtract(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Add money to the account (deposit)
     *
     * @param amount Amount to add
     * @throws AccountNotActiveException if account is not ACTIVE
     */
    public void credit(BigDecimal amount) {
        // Business Rule: Account must be active
        if (!isActive()) {
            throw new AccountNotActiveException(
                    "Cannot credit to account " + id + ". Account status: " + status
            );
        }

        // Add the amount
        this.balance = this.balance.add(amount);
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Check if account is active
     *
     * @return true if account status is ACTIVE
     */
    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }
}