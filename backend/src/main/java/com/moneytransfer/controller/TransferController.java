package com.moneytransfer.controller;

import com.moneytransfer.dto.TransferRequest;
import com.moneytransfer.dto.TransferResponse;
import com.moneytransfer.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for money transfer operations
 *
 * Base URL: /api/v1/transfers
 */
@RestController  // Marks this as a REST controller
@RequestMapping("/api/v1/transfers")  // Base path for all endpoints
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferService transferService;

    /**
     * Execute a money transfer
     *
     * POST /api/v1/transfers
     *
     * Request Body:
     * {
     *   "fromAccountId": 1,
     *   "toAccountId": 2,
     *   "amount": 500.00,
     *   "idempotencyKey": "unique-key"
     * }
     */
    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        log.info("Transfer request received: {}", request);

        TransferResponse response = transferService.transfer(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}