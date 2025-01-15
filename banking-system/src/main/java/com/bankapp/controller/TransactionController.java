package com.bankapp.controller;

import com.bankapp.dto.TransactionRequestDto;
import com.bankapp.dto.TransactionResponseDto;
import com.bankapp.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountNumber}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(
            Authentication authentication,
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDto request) {
        TransactionResponseDto transaction = transactionService.deposit(
            authentication.getName(), accountNumber, request);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(
            Authentication authentication,
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDto request) {
        TransactionResponseDto transaction = transactionService.withdraw(
            authentication.getName(), accountNumber, request);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transfer(
            Authentication authentication,
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequestDto request) {
        TransactionResponseDto transaction = transactionService.transfer(
            authentication.getName(), accountNumber, request);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getTransactions(
            Authentication authentication,
            @PathVariable String accountNumber,
            Pageable pageable) {
        Page<TransactionResponseDto> transactions = transactionService.getAccountTransactions(
            authentication.getName(), accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }
}