package com.bankapp.controller;

import com.bankapp.dto.AccountCreateDto;
import com.bankapp.dto.AccountResponseDto;
import com.bankapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            Authentication authentication,
            @Valid @RequestBody AccountCreateDto createDto) {
        AccountResponseDto account = accountService.createAccount(authentication.getName(), createDto);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getUserAccounts(Authentication authentication) {
        List<AccountResponseDto> accounts = accountService.getUserAccounts(authentication.getName());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(
            Authentication authentication,
            @PathVariable Long id) {
        AccountResponseDto account = accountService.getAccountById(authentication.getName(), id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByNumber(
            Authentication authentication,
            @PathVariable String accountNumber) {
        AccountResponseDto account = accountService.getAccountByNumber(authentication.getName(), accountNumber);
        return ResponseEntity.ok(account);
    }
}