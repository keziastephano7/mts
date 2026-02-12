-- Seed Data for Development and Testing
-- This creates sample accounts for testing

-- Insert test accounts
INSERT INTO accounts (holder_name, balance, status, version, last_updated) VALUES
                                                                               ('John Doe', 10000.00, 'ACTIVE', 0, CURRENT_TIMESTAMP),
                                                                               ('Jane Smith', 5000.00, 'ACTIVE', 0, CURRENT_TIMESTAMP),
                                                                               ('Bob Wilson', 15000.00, 'ACTIVE', 0, CURRENT_TIMESTAMP),
                                                                               ('Alice Brown', 2000.00, 'LOCKED', 0, CURRENT_TIMESTAMP),
                                                                               ('Charlie Davis', 0.00, 'CLOSED', 0, CURRENT_TIMESTAMP);

-- Note: Transaction logs will be created as transfers are made