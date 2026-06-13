package com.moneytransfer.repository;

import com.moneytransfer.domain.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Reward entity
 */
@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    /**
     * Find reward record by account ID
     * @param accountId Account ID
     * @return Optional containing Reward if found
     */
    Optional<Reward> findByAccountId(Long accountId);
}
