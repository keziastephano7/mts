package com.moneytransfer.repository;

import com.moneytransfer.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Account entity

 * JpaRepository provides built-in methods:
 * - findById(Long id)
 * - findAll()
 * - save(Account account)
 * - deleteById(Long id)
 * - count()

 * Spring automatically implements this interface!
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Spring automatically generates the implementation

    // Custom query methods can be added by following naming conventions
    // Example: findByHolderName would search by holder name
}