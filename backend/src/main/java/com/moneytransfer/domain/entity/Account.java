package com.moneytransfer.domain.entity;

import com.moneytransfer.domain.enums.AccountStatus;
import com.moneytransfer.exception.AccountNotActiveException;
import com.moneytransfer.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a bank account in the money transfer system
 */
@Entity  // JPA: This class maps to a database table
@Table(name = "accounts")  // JPA: Table name in database
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id  // JPA: Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA: Auto-increment
    private Long id;

    @Column(name = "holder_name", nullable = false)  // JPA: Column mapping
    private String holderName;

    @Column(nullable = false, precision = 18, scale = 2)  // JPA: Decimal precision
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)  // JPA: Store enum as string in DB
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @Version  // JPA: Optimistic locking - prevents concurrent updates
    private Integer version;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    /**
     * Automatically set lastUpdated before persisting or updating
     */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Deduct money from the account
     */
    public void debit(BigDecimal amount) {
        if (!isActive()) {
            throw new AccountNotActiveException("Cannot debit from account " + id + ". Account status: " + status);
        }

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + balance + ", Required: " + amount);
        }

        this.balance = this.balance.subtract(amount);
    }

    /**
     * Add money to the account
     */
    public void credit(BigDecimal amount) {
        if (!isActive()) {
            throw new AccountNotActiveException("Cannot credit to account " + id + ". Account status: " + status);
        }

        this.balance = this.balance.add(amount);
    }

    /**
     * Check if account is active
     */
    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }
}