package com.bankapp.dto;

import com.bankapp.model.AccountType;
import java.math.BigDecimal;

public class AccountResponseDto {
    private Long id;
    private String accountNumber;
    private AccountType type;
    private BigDecimal balance;
    private boolean active;

    // Constructors
    public AccountResponseDto() {
    }

    public AccountResponseDto(Long id, String accountNumber, AccountType type, BigDecimal balance, boolean active) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.balance = balance;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}