package com.moneytransfer.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the reward points balance for an account
 */
@Entity
@Table(name = "rewards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false, unique = true)
    private Long accountId;

    @Column(name = "points_balance", nullable = false)
    private Long pointsBalance = 0L;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    /**
     * Add reward points to the account
     * @param points Points to add (must be positive)
     */
    public void addPoints(Long points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points must be non-negative");
        }
        this.pointsBalance += points;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Deduct reward points from the account
     * @param points Points to deduct
     * @throws IllegalArgumentException if insufficient points
     */
    public void deductPoints(Long points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points must be non-negative");
        }
        if (this.pointsBalance < points) {
            throw new IllegalArgumentException("Insufficient reward points. Current: " + this.pointsBalance + ", Requested: " + points);
        }
        this.pointsBalance -= points;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Automatically set lastUpdated before persisting or updating
     */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        if (this.lastUpdated == null) {
            this.lastUpdated = LocalDateTime.now();
        }
    }
}
