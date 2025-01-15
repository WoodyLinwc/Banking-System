package com.bankapp.service;

import com.bankapp.dto.AccountCreateDto;
import com.bankapp.dto.AccountResponseDto;
import com.bankapp.model.Account;
import com.bankapp.model.User;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponseDto createAccount(String userEmail, AccountCreateDto createDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setType(createDto.getType());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        account.setActive(true);

        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }

    public List<AccountResponseDto> getUserAccounts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        return accountRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AccountResponseDto getAccountById(String userEmail, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        
        // Security check: ensure the account belongs to the user
        if (!account.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("Account doesn't belong to the user");
        }
        
        return convertToDto(account);
    }

    public AccountResponseDto getAccountByNumber(String userEmail, String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        
        // Security check: ensure the account belongs to the user
        if (!account.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("Account doesn't belong to the user");
        }
        
        return convertToDto(account);
    }

    private String generateAccountNumber() {
        // Generate a random 10-digit account number
        Random random = new Random();
        StringBuilder accountNumber;
        do {
            accountNumber = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                accountNumber.append(random.nextInt(10));
            }
        } while (accountRepository.existsByAccountNumber(accountNumber.toString()));
        
        return accountNumber.toString();
    }

    private AccountResponseDto convertToDto(Account account) {
        return new AccountResponseDto(
            account.getId(),
            account.getAccountNumber(),
            account.getType(),
            account.getBalance(),
            account.isActive()
        );
    }
}