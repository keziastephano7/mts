package com.moneytransfer.config;

import com.moneytransfer.domain.entity.Account;
import com.moneytransfer.domain.enums.AccountStatus;
import com.moneytransfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Initializes database with seed data on application startup
 *
 * CommandLineRunner runs automatically after Spring Boot starts
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        log.info("Initializing database with seed data...");

        // Clear existing data (for development)
        accountRepository.deleteAll();

        // Create test accounts
        createAccount("John Doe", new BigDecimal("10000.00"), AccountStatus.ACTIVE);
        createAccount("Jane Smith", new BigDecimal("5000.00"), AccountStatus.ACTIVE);
        createAccount("Bob Wilson", new BigDecimal("15000.00"), AccountStatus.ACTIVE);
        createAccount("Alice Brown", new BigDecimal("2000.00"), AccountStatus.LOCKED);
        createAccount("Charlie Davis", new BigDecimal("0.00"), AccountStatus.CLOSED);

        log.info("✓ Database initialized with {} accounts", accountRepository.count());

        // Display account details
        accountRepository.findAll().forEach(account -> {
            log.info("  → Account {}: {} - Balance: ${} - Status: {}",
                    account.getId(),
                    account.getHolderName(),
                    account.getBalance(),
                    account.getStatus());
        });
    }

    /**
     * Helper method to create and save an account
     */
    private void createAccount(String holderName, BigDecimal balance, AccountStatus status) {
        Account account = new Account();
        account.setHolderName(holderName);
        account.setBalance(balance);
        account.setStatus(status);
        account.setVersion(0);
        account.setLastUpdated(LocalDateTime.now());

        accountRepository.save(account);
    }
}