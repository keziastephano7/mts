package com.moneytransfer.controller;

import com.moneytransfer.domain.entity.TransactionLog;
import com.moneytransfer.dto.AccountResponse;
import com.moneytransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for account operations
 *
 * Base URL: /api/v1/accounts
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /**
     * Get account details
     *
     * GET /api/v1/accounts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        log.info("Get account request: {}", id);

        AccountResponse account = accountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    /**
     * Get account balance
     *
     * GET /api/v1/accounts/{id}/balance
     */
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        log.info("Get balance request: {}", id);

        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(balance);
    }

    /**
     * Get transaction history
     *
     * GET /api/v1/accounts/{id}/transactions
     */
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionLog>> getTransactions(@PathVariable Long id) {
        log.info("Get transactions request: {}", id);

        List<TransactionLog> transactions = accountService.getTransactions(id);
        return ResponseEntity.ok(transactions);
    }
}