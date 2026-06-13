package com.moneytransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for reward balance information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardBalanceDTO {
    private Long accountId;
    private Long pointsBalance;
}
