package com.moneytransfer.domain.entity;

import com.moneytransfer.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder  // Lombok: Enables builder pattern for easy object creation
public class TransactionLog {
    private UUID id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private String failureReason;
    private String idempotencyKey;
    private LocalDateTime createdOn;
}