-- Money Transfer System - Database Schema
-- This file documents the database structure
-- (Spring Boot auto-creates tables from entities)

-- ACCOUNTS Table
CREATE TABLE accounts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          holder_name VARCHAR(255) NOT NULL,
                          balance DECIMAL(18,2) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          version INT DEFAULT 0,
                          last_updated TIMESTAMP,
                          CONSTRAINT chk_balance CHECK (balance >= 0),
                          CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'LOCKED', 'CLOSED'))
);

-- TRANSACTION_LOGS Table
CREATE TABLE transaction_logs (
                                  id VARCHAR(36) PRIMARY KEY,
                                  from_account_id BIGINT NOT NULL,
                                  to_account_id BIGINT NOT NULL,
                                  amount DECIMAL(18,2) NOT NULL,
                                  status VARCHAR(20) NOT NULL,
                                  failure_reason VARCHAR(255),
                                  idempotency_key VARCHAR(100) UNIQUE NOT NULL,
                                  created_on TIMESTAMP NOT NULL,
                                  CONSTRAINT fk_from_account FOREIGN KEY (from_account_id) REFERENCES accounts(id),
                                  CONSTRAINT fk_to_account FOREIGN KEY (to_account_id) REFERENCES accounts(id),
                                  CONSTRAINT chk_amount CHECK (amount > 0),
                                  CONSTRAINT chk_transaction_status CHECK (status IN ('SUCCESS', 'FAILED'))
);

-- Indexes for better query performance
CREATE INDEX idx_from_account ON transaction_logs(from_account_id);
CREATE INDEX idx_to_account ON transaction_logs(to_account_id);
CREATE INDEX idx_idempotency ON transaction_logs(idempotency_key);
CREATE INDEX idx_created_on ON transaction_logs(created_on);