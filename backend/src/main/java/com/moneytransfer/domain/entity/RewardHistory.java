package com.moneytransfer.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Audit trail for reward point grants
 * Each entry represents a successful reward grant linked to a transaction
 */
@Entity
@Table(name = "reward_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "points_granted", nullable = false)
    private Long pointsGranted;

    @Column(name = "granted_on", nullable = false)
    private LocalDateTime grantedOn;

    /**
     * Automatically set grantedOn if not provided
     */
    @PrePersist
    protected void onCreate() {
        if (this.grantedOn == null) {
            this.grantedOn = LocalDateTime.now();
        }
    }
}
