package com.moneytransfer.service;

import com.moneytransfer.domain.entity.Reward;
import com.moneytransfer.domain.entity.RewardHistory;
import com.moneytransfer.domain.entity.TransactionLog;
import com.moneytransfer.domain.enums.TransactionStatus;
import com.moneytransfer.dto.RewardHistoryDTO;
import com.moneytransfer.repository.RewardRepository;
import com.moneytransfer.repository.RewardHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to manage reward points for users
 * 
 * Reward Rules:
 * 1. Transaction must be SUCCESS
 * 2. Amount must be > 100 rupees
 * 3. Sender and receiver must be different (no self-transfers)
 * 4. Rewards are granted to the sender (not receiver)
 * 
 * Calculation: 1 point per 100 rupees (rounded down)
 * Example: 250 rupees = 2 points, 99 rupees = 0 points
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RewardService {

    private final RewardRepository rewardRepository;
    private final RewardHistoryRepository rewardHistoryRepository;

    private static final BigDecimal REWARD_THRESHOLD = new BigDecimal("100");
    private static final long POINTS_PER_HUNDRED = 1L;

    /**
     * Calculate reward points for a transaction
     * 
     * @param amount Transaction amount
     * @return Points to be awarded (0 if not eligible)
     */
    public long calculateRewardPoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(REWARD_THRESHOLD) <= 0) {
            return 0L;
        }
        // 1 point per 100 rupees, rounded down
        return amount.divide(REWARD_THRESHOLD).longValue() * POINTS_PER_HUNDRED;
    }

    /**
     * Check if a transaction is eligible for rewards
     * 
     * Eligibility criteria:
     * 1. Status is SUCCESS
     * 2. Amount > 100 rupees
     * 3. Sender and receiver are different users
     * 
     * @param transaction The transaction to check
     * @return true if eligible, false otherwise
     */
    public boolean isEligibleForReward(TransactionLog transaction) {
        if (transaction == null) {
            return false;
        }

        boolean statusValid = TransactionStatus.SUCCESS == transaction.getStatus();
        boolean amountValid = transaction.getAmount().compareTo(REWARD_THRESHOLD) > 0;
        boolean notSelfTransfer = !transaction.getFromAccountId().equals(transaction.getToAccountId());

        return statusValid && amountValid && notSelfTransfer;
    }

    /**
     * Grant reward points for a successful transaction
     * This method:
     * 1. Checks if transaction is eligible
     * 2. Calculates reward points
     * 3. Updates sender's reward balance
     * 4. Records the reward in history
     * 
     * @param transaction The transaction to grant rewards for
     * @return true if reward was granted, false if not eligible
     */
    @Transactional
    public boolean grantReward(TransactionLog transaction) {
        // Check eligibility
        if (!isEligibleForReward(transaction)) {
            log.debug("Transaction {} not eligible for rewards", transaction.getId());
            return false;
        }

        // Check if reward already granted
        if (rewardHistoryRepository.countByTransactionId(transaction.getId()) > 0) {
            log.warn("Reward already granted for transaction {}", transaction.getId());
            return false;
        }

        // Calculate points
        long pointsToGrant = calculateRewardPoints(transaction.getAmount());
        if (pointsToGrant == 0) {
            log.debug("No points to grant for transaction {}", transaction.getId());
            return false;
        }

        // Get or create reward record for sender
        Long senderAccountId = transaction.getFromAccountId();
        Reward reward = rewardRepository.findByAccountId(senderAccountId)
                .orElse(new Reward());
        
        if (reward.getId() == null) {
            reward.setAccountId(senderAccountId);
            reward.setPointsBalance(0L);
        }

        // Add points
        reward.addPoints(pointsToGrant);
        reward = rewardRepository.save(reward);

        // Record in history
        RewardHistory history = RewardHistory.builder()
                .accountId(senderAccountId)
                .transactionId(transaction.getId())
                .pointsGranted(pointsToGrant)
                .build();
        rewardHistoryRepository.save(history);

        log.info("Granted {} reward points to account {} for transaction {}",
                pointsToGrant, senderAccountId, transaction.getId());

        return true;
    }

    /**
     * Get current reward points balance for an account
     * 
     * @param accountId Account ID
     * @return Points balance (0 if no reward record exists)
     */
    public long getRewardBalance(Long accountId) {
        return rewardRepository.findByAccountId(accountId)
                .map(Reward::getPointsBalance)
                .orElse(0L);
    }

    /**
     * Get reward history for an account
     * 
     * @param accountId Account ID
     * @return List of reward history entries, ordered by most recent first
     */
    public List<RewardHistoryDTO> getRewardHistory(Long accountId) {
        List<RewardHistory> history = rewardHistoryRepository.findByAccountIdOrderByGrantedOnDesc(accountId);
        return history.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert RewardHistory entity to DTO
     */
    private RewardHistoryDTO convertToDTO(RewardHistory history) {
        return RewardHistoryDTO.builder()
                .id(history.getId())
                .transactionId(history.getTransactionId())
                .pointsGranted(history.getPointsGranted())
                .grantedOn(history.getGrantedOn())
                .build();
    }

    /**
     * Deduct reward points from an account (for future redemptions)
     * 
     * @param accountId Account ID
     * @param points Points to deduct
     * @return true if successful
     * @throws IllegalArgumentException if insufficient points
     */
    @Transactional
    public boolean deductRewardPoints(Long accountId, Long points) {
        Reward reward = rewardRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No reward record found for account: " + accountId));

        reward.deductPoints(points);
        rewardRepository.save(reward);

        log.info("Deducted {} reward points from account {}", points, accountId);
        return true;
    }
}
