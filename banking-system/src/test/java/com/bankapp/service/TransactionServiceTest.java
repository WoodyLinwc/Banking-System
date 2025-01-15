package com.bankapp.service;

import com.bankapp.dto.TransactionRequestDto;
import com.bankapp.dto.TransactionResponseDto;
import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import com.bankapp.model.User;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    private TransactionService transactionService;
    private User mockUser;
    private Account sourceAccount;
    private Account destinationAccount;
    private TransactionRequestDto transactionRequest;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(
            transactionRepository,
            accountRepository,
            userRepository,
            emailService
        );

        // Setup test data
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("john@example.com");

        sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setAccountNumber("1234567890");
        sourceAccount.setBalance(new BigDecimal("1000.00"));
        sourceAccount.setUser(mockUser);
        sourceAccount.setActive(true);

        destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setAccountNumber("0987654321");
        destinationAccount.setBalance(new BigDecimal("500.00"));
        destinationAccount.setUser(mockUser);
        destinationAccount.setActive(true);

        transactionRequest = new TransactionRequestDto();
        transactionRequest.setAmount(new BigDecimal("100.00"));
        transactionRequest.setDescription("Test transaction");

        // Setup common mocks with lenient mode
        lenient().when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockUser));
        lenient().when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        lenient().doNothing().when(emailService).sendTransferNotification(any(), any(), any(), any());
    }

    @Test
    void deposit_Success() {
        // Arrange
        lenient().when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(sourceAccount));
        lenient().when(accountRepository.save(any(Account.class))).thenReturn(sourceAccount);

        // Act
        TransactionResponseDto result = transactionService.deposit(
            mockUser.getEmail(), 
            sourceAccount.getAccountNumber(), 
            transactionRequest
        );

        // Assert
        assertNotNull(result);
        assertEquals(transactionRequest.getAmount(), result.getAmount());
        assertEquals(new BigDecimal("1100.00"), sourceAccount.getBalance());
    }

    @Test
    void withdraw_Success() {
        // Arrange
        lenient().when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(sourceAccount));
        lenient().when(accountRepository.save(any(Account.class))).thenReturn(sourceAccount);

        // Act
        TransactionResponseDto result = transactionService.withdraw(
            mockUser.getEmail(), 
            sourceAccount.getAccountNumber(), 
            transactionRequest
        );

        // Assert
        assertNotNull(result);
        assertEquals(transactionRequest.getAmount(), result.getAmount());
        assertEquals(new BigDecimal("900.00"), sourceAccount.getBalance());
    }

    @Test
    void withdraw_InsufficientFunds() {
        // Arrange
        sourceAccount.setBalance(new BigDecimal("50.00"));
        lenient().when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(sourceAccount));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.withdraw(
                mockUser.getEmail(),
                sourceAccount.getAccountNumber(),
                transactionRequest
            );
        });
    }

    @Test
    void transfer_Success() {
        // Arrange
        transactionRequest.setDestinationAccountNumber(destinationAccount.getAccountNumber());
        
        lenient().when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber()))
            .thenReturn(Optional.of(sourceAccount));
        lenient().when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber()))
            .thenReturn(Optional.of(destinationAccount));
        lenient().when(accountRepository.save(any(Account.class)))
            .thenAnswer(i -> i.getArguments()[0]);

        // Act
        TransactionResponseDto result = transactionService.transfer(
            mockUser.getEmail(),
            sourceAccount.getAccountNumber(),
            transactionRequest
        );

        // Assert
        assertNotNull(result);
        assertEquals(transactionRequest.getAmount(), result.getAmount());
        assertEquals(new BigDecimal("900.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("600.00"), destinationAccount.getBalance());
    }
}