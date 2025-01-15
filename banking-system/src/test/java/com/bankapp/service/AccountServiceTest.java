package com.bankapp.service;

import com.bankapp.dto.AccountCreateDto;
import com.bankapp.dto.AccountResponseDto;
import com.bankapp.model.Account;
import com.bankapp.model.AccountType;
import com.bankapp.model.User;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    private AccountService accountService;
    private User mockUser;
    private Account mockAccount;
    private AccountCreateDto createDto;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository, userRepository);

        // Setup test data
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("john@example.com");

        mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber("1234567890");
        mockAccount.setType(AccountType.SAVINGS);
        mockAccount.setBalance(BigDecimal.ZERO);
        mockAccount.setUser(mockUser);
        mockAccount.setActive(true);

        createDto = new AccountCreateDto();
        createDto.setType(AccountType.SAVINGS);

        // Setup common mocks with lenient mode
        lenient().when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));
        lenient().when(accountRepository.findById(any())).thenReturn(Optional.of(mockAccount));
    }

    @Test
    void createAccount_Success() {
        // Arrange
        lenient().when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

        // Act
        AccountResponseDto result = accountService.createAccount("john@example.com", createDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockAccount.getAccountNumber(), result.getAccountNumber());
        assertEquals(mockAccount.getType(), result.getType());
        assertEquals(mockAccount.getBalance(), result.getBalance());
    }

    @Test
    void createAccount_UserNotFound() {
        // Arrange
        lenient().when(userRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            accountService.createAccount("nonexistent@example.com", createDto);
        });
    }

    @Test
    void getUserAccounts_Success() {
        // Arrange
        Account secondAccount = new Account();
        secondAccount.setId(2L);
        secondAccount.setAccountNumber("0987654321");
        secondAccount.setType(AccountType.CHECKING);
        secondAccount.setBalance(BigDecimal.ZERO);
        secondAccount.setUser(mockUser);

        lenient().when(accountRepository.findByUser(any()))
                .thenReturn(Arrays.asList(mockAccount, secondAccount));

        // Act
        List<AccountResponseDto> results = accountService.getUserAccounts("john@example.com");

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void getAccountById_Success() {
        // Act
        AccountResponseDto result = accountService.getAccountById("john@example.com", 1L);

        // Assert
        assertNotNull(result);
        assertEquals(mockAccount.getAccountNumber(), result.getAccountNumber());
    }

    @Test
    void getAccountById_NotFound() {
        // Arrange
        lenient().when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountById("john@example.com", 999L);
        });
    }
}