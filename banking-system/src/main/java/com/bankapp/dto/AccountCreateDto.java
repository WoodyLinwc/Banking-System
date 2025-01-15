package com.bankapp.dto;

import com.bankapp.model.AccountType;
import jakarta.validation.constraints.NotNull;

public class AccountCreateDto {
    @NotNull(message = "Account type is required")
    private AccountType type;

    // Constructors
    public AccountCreateDto() {
    }

    public AccountCreateDto(AccountType type) {
        this.type = type;
    }

    // Getter and Setter
    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}