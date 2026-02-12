package com.moneytransfer.service;

import com.moneytransfer.domain.entity.Account;
import com.moneytransfer.domain.entity.TransactionLog;
import com.moneytransfer.dto.AccountResponse;
import com.moneytransfer.exception.AccountNotFoundException;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    /**
     * Get account details by ID
     */
    public AccountResponse getAccount(Long accountId) {
        log.info("Fetching account: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return AccountResponse.builder()
                .id(account.getId())
                .holderName(account.getHolderName())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .build();
    }

    /**
     * Get account balance
     */
    public BigDecimal getBalance(Long accountId) {
        log.info("Fetching balance for account: {}", accountId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return account.getBalance();
    }

    /**
     * Get transaction history for an account
     */
    public List<TransactionLog> getTransactions(Long accountId) {
        log.info("Fetching transactions for account: {}", accountId);

        // Verify account exists
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return transactionLogRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }
}