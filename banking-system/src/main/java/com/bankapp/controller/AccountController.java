package com.bankapp.controller;

import com.bankapp.dto.AccountCreateDto;
import com.bankapp.dto.AccountResponseDto;
import com.bankapp.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
        summary = "Create a new account",
        description = "Creates a new bank account for the authenticated user"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Account created successfully",
            content = @Content(schema = @Schema(implementation = AccountResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input"
        )
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            Authentication authentication,
            @Valid @RequestBody AccountCreateDto createDto) {
        AccountResponseDto account = accountService.createAccount(authentication.getName(), createDto);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Get all user accounts",
        description = "Retrieves all accounts belonging to the authenticated user"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved accounts",
            content = @Content(schema = @Schema(implementation = AccountResponseDto.class))
        )
    })
    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getUserAccounts(Authentication authentication) {
        List<AccountResponseDto> accounts = accountService.getUserAccounts(authentication.getName());
        return ResponseEntity.ok(accounts);
    }

    @Operation(
        summary = "Get account by ID",
        description = "Retrieves a specific account by its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved account",
            content = @Content(schema = @Schema(implementation = AccountResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(
            Authentication authentication,
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long id) {
        AccountResponseDto account = accountService.getAccountById(authentication.getName(), id);
        return ResponseEntity.ok(account);
    }
}