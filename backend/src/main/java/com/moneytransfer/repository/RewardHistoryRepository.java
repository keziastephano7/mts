package com.moneytransfer.repository;

import com.moneytransfer.domain.entity.RewardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for RewardHistory entity
 */
@Repository
public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {

    /**
     * Find all reward history entries for an account, ordered by most recent first
     * @param accountId Account ID
     * @return List of reward history entries
     */
    List<RewardHistory> findByAccountIdOrderByGrantedOnDesc(Long accountId);

    /**
     * Check if reward already granted for a transaction
     * @param transactionId Transaction ID
     * @return Count of entries for this transaction
     */
    Long countByTransactionId(String transactionId);
}
