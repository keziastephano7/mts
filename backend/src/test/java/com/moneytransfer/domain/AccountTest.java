package com.moneytransfer.domain;

import com.moneytransfer.domain.entity.Account;
import com.moneytransfer.domain.enums.AccountStatus;
import com.moneytransfer.exception.AccountNotActiveException;
import com.moneytransfer.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Account entity
 * Tests business logic in debit() and credit() methods
 */
class AccountTest {

    private Account account;

    /**
     * Set up a test account before each test
     */
    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setHolderName("John Doe");
        account.setBalance(new BigDecimal("1000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setVersion(0);
        account.setLastUpdated(LocalDateTime.now());
    }

    /**
     * Test successful debit (withdrawal)
     */
    @Test
    void testDebit_Success() {
        // Arrange
        BigDecimal debitAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("500.00");

        // Act
        account.debit(debitAmount);

        // Assert
        assertEquals(expectedBalance, account.getBalance());
    }

    /**
     * Test debit with insufficient balance - should throw exception
     */
    @Test
    void testDebit_InsufficientBalance() {
        // Arrange
        BigDecimal debitAmount = new BigDecimal("1500.00");  // More than balance

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            account.debit(debitAmount);
        });
    }

    /**
     * Test debit on locked account - should throw exception
     */
    @Test
    void testDebit_AccountLocked() {
        // Arrange
        account.setStatus(AccountStatus.LOCKED);
        BigDecimal debitAmount = new BigDecimal("100.00");

        // Act & Assert
        assertThrows(AccountNotActiveException.class, () -> {
            account.debit(debitAmount);
        });
    }

    /**
     * Test successful credit (deposit)
     */
    @Test
    void testCredit_Success() {
        // Arrange
        BigDecimal creditAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("1500.00");

        // Act
        account.credit(creditAmount);

        // Assert
        assertEquals(expectedBalance, account.getBalance());
    }

    /**
     * Test credit on closed account - should throw exception
     */
    @Test
    void testCredit_AccountClosed() {
        // Arrange
        account.setStatus(AccountStatus.CLOSED);
        BigDecimal creditAmount = new BigDecimal("100.00");

        // Act & Assert
        assertThrows(AccountNotActiveException.class, () -> {
            account.credit(creditAmount);
        });
    }

    /**
     * Test isActive() method
     */
    @Test
    void testIsActive() {
        // Test ACTIVE status
        account.setStatus(AccountStatus.ACTIVE);
        assertTrue(account.isActive());

        // Test LOCKED status
        account.setStatus(AccountStatus.LOCKED);
        assertFalse(account.isActive());

        // Test CLOSED status
        account.setStatus(AccountStatus.CLOSED);
        assertFalse(account.isActive());
    }
}