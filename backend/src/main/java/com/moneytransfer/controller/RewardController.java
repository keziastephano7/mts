package com.moneytransfer.controller;

import com.moneytransfer.dto.RewardBalanceDTO;
import com.moneytransfer.dto.RewardHistoryDTO;
import com.moneytransfer.service.RewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for reward operations
 *
 * Base URL: /api/v1/rewards
 */
@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
@Slf4j
public class RewardController {

    private final RewardService rewardService;

    /**
     * Get reward points balance for an account
     *
     * GET /api/v1/rewards/{accountId}/balance
     *
     * Response:
     * {
     *   "accountId": 1,
     *   "pointsBalance": 150
     * }
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<RewardBalanceDTO> getRewardBalance(@PathVariable Long accountId) {
        log.info("Getting reward balance for account: {}", accountId);

        long pointsBalance = rewardService.getRewardBalance(accountId);

        RewardBalanceDTO response = RewardBalanceDTO.builder()
                .accountId(accountId)
                .pointsBalance(pointsBalance)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Get reward history for an account
     *
     * GET /api/v1/rewards/{accountId}/history
     *
     * Response:
     * [
     *   {
     *     "id": 1,
     *     "transactionId": "uuid-string",
     *     "pointsGranted": 2,
     *     "grantedOn": "2025-06-13T10:30:00"
     *   }
     * ]
     */
    @GetMapping("/{accountId}/history")
    public ResponseEntity<List<RewardHistoryDTO>> getRewardHistory(@PathVariable Long accountId) {
        log.info("Getting reward history for account: {}", accountId);

        List<RewardHistoryDTO> history = rewardService.getRewardHistory(accountId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(history);
    }
}
