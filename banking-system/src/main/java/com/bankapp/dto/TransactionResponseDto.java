package com.bankapp.dto;

import com.bankapp.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDto {
    private Long id;
    private String referenceNumber;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDateTime timestamp;
    private String accountNumber;
    private String destinationAccountNumber;
    private BigDecimal balanceAfterTransaction;

    // Constructors
    public TransactionResponseDto() {
    }

    public TransactionResponseDto(Long id, String referenceNumber, BigDecimal amount, 
                                TransactionType type, String description, LocalDateTime timestamp,
                                String accountNumber, String destinationAccountNumber, 
                                BigDecimal balanceAfterTransaction) {
        this.id = id;
        this.referenceNumber = referenceNumber;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
        this.accountNumber = accountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public BigDecimal getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(BigDecimal balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }
}