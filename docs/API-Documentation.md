# Money Transfer System - API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
All endpoints require Basic Authentication:
- Username: `admin`
- Password: `admin123`

## Endpoints

### 1. Get Account Details
```http
GET /accounts/{id}
```

**Response:**
```json
{
  "id": 1,
  "holderName": "John Doe",
  "balance": 10000.00,
  "status": "ACTIVE"
}
```

### 2. Get Account Balance
```http
GET /accounts/{id}/balance
```

**Response:**
```json
10000.00
```

### 3. Get Transaction History
```http
GET /accounts/{id}/transactions
```

**Response:**
```json
[
  {
    "id": "uuid",
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 500.00,
    "status": "SUCCESS",
    "createdOn": "2024-01-01T10:00:00"
  }
]
```

### 4. Transfer Money
```http
POST /transfers
```

**Request Body:**
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 500.00,
  "idempotencyKey": "unique-key-123"
}
```

**Success Response (200):**
```json
{
  "transactionId": "uuid",
  "status": "SUCCESS",
  "message": "Transfer completed successfully",
  "debitedFrom": 1,
  "creditedTo": 2,
  "amount": 500.00
}
```

**Error Responses:**

**404 - Account Not Found**
```json
{
  "errorCode": "ACC-404",
  "message": "Account not found with ID: 999",
  "timestamp": "2024-01-01T10:00:00"
}
```

**400 - Insufficient Balance**
```json
{
  "errorCode": "TRX-400",
  "message": "Insufficient balance. Available: 100.00, Required: 500.00",
  "timestamp": "2024-01-01T10:00:00"
}
```

**409 - Duplicate Transfer**
```json
{
  "errorCode": "TRX-409",
  "message": "Duplicate transfer detected with idempotency key: abc-123",
  "timestamp": "2024-01-01T10:00:00"
}
```

**403 - Account Not Active**
```json
{
  "errorCode": "ACC-403",
  "message": "Cannot debit from account 4. Account status: LOCKED",
  "timestamp": "2024-01-01T10:00:00"
}
```

## Business Rules

1. Accounts must be different
2. Source account must exist
3. Destination account must exist
4. Source account must be ACTIVE
5. Destination account must be ACTIVE
6. Amount must be > 0
7. Source balance >= amount
8. Idempotency key must be unique
9. Debit happens before credit
10. Every transfer is logged

## Test Accounts

| ID | Name | Balance | Status |
|----|------|---------|--------|
| 1 | John Doe | $10,000.00 | ACTIVE |
| 2 | Jane Smith | $5,000.00 | ACTIVE |
| 3 | Bob Wilson | $15,000.00 | ACTIVE |
| 4 | Alice Brown | $2,000.00 | LOCKED |
| 5 | Charlie Davis | $0.00 | CLOSED |