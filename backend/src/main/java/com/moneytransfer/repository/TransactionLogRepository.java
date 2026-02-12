package com.moneytransfer.repository;

import com.moneytransfer.domain.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for TransactionLog entity
 */
@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, String> {

    /**
     * Find transaction by idempotency key
     * Used to prevent duplicate transfers
     */
    Optional<TransactionLog> findByIdempotencyKey(String idempotencyKey);

    /**
     * Find all transactions for a specific account (as sender or receiver)
     *
     * @param accountId Account ID
     * @return List of transactions
     */
    List<TransactionLog> findByFromAccountIdOrToAccountId(Long accountId, Long accountId2);
}