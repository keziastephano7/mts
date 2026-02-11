package com.moneytransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response object returned after a transfer attempt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {
    private UUID transactionId;
    private String message;
    private String status;
    private Long debitedFrom;
    private Long creditedTo;
    private BigDecimal amount;
}