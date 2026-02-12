package com.moneytransfer.service;

import com.moneytransfer.domain.entity.Account;
import com.moneytransfer.domain.entity.TransactionLog;
import com.moneytransfer.domain.enums.TransactionStatus;
import com.moneytransfer.dto.TransferRequest;
import com.moneytransfer.dto.TransferResponse;
import com.moneytransfer.exception.AccountNotFoundException;
import com.moneytransfer.exception.DuplicateTransferException;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service  // Marks this as a Spring service component
@RequiredArgsConstructor  // Lombok: Generates constructor for final fields
@Slf4j  // Lombok: Provides logging (log.info, log.error, etc.)
public class TransferService {

    // Spring automatically injects these dependencies
    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        log.info("Processing transfer: {} -> {}, amount: {}",
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());

        try {
            // Step 1: Check for duplicate transfer (idempotency)
            checkIdempotency(request.getIdempotencyKey());

            // Step 2: Validate the transfer
            validateTransfer(request);

            // Step 3: Execute the transfer
            TransactionLog transaction = executeTransfer(request);

            // Step 4: Return success response
            return TransferResponse.builder()
                    .transactionId(java.util.UUID.fromString(transaction.getId()))
                    .status("SUCCESS")
                    .message("Transfer completed successfully")
                    .debitedFrom(request.getFromAccountId())
                    .creditedTo(request.getToAccountId())
                    .amount(request.getAmount())
                    .build();

        } catch (Exception e) {
            log.error("Transfer failed: {}", e.getMessage());

            // Log the failed transaction
            TransactionLog failedTransaction = TransactionLog.builder()
                    .fromAccountId(request.getFromAccountId())
                    .toAccountId(request.getToAccountId())
                    .amount(request.getAmount())
                    .status(TransactionStatus.FAILED)
                    .failureReason(e.getMessage())
                    .idempotencyKey(request.getIdempotencyKey())
                    .build();

            transactionLogRepository.save(failedTransaction);

            // Re-throw the exception
            throw e;
        }
    }

    /**
     * Check if this transfer was already processed
     */
    private void checkIdempotency(String idempotencyKey) {
        transactionLogRepository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(transaction -> {
                    throw new DuplicateTransferException(idempotencyKey);
                });
    }

    /**
     * Validate transfer request
     */
    private void validateTransfer(TransferRequest request) {
        // Business Rule: Accounts must be different
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Business Rule: Amount must be positive
        if (request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
    }

    /**
     * Execute the actual transfer
     * This method performs the debit and credit operations
     */
    private TransactionLog executeTransfer(TransferRequest request) {
        // Fetch accounts
        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountId()));

        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getToAccountId()));

        // Business Rule: Debit before credit (important for accounting)
        fromAccount.debit(request.getAmount());
        toAccount.credit(request.getAmount());

        // Save updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create transaction log
        TransactionLog transaction = TransactionLog.builder()
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .amount(request.getAmount())
                .status(TransactionStatus.SUCCESS)
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        return transactionLogRepository.save(transaction);
    }
}