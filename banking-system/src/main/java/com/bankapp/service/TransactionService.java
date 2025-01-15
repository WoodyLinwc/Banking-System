package com.bankapp.service;

import com.bankapp.dto.TransactionRequestDto;
import com.bankapp.dto.TransactionResponseDto;
import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import com.bankapp.model.TransactionType;
import com.bankapp.model.User;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, 
                            AccountRepository accountRepository,
                            UserRepository userRepository,
                            EmailService emailService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public TransactionResponseDto deposit(String userEmail, String accountNumber, TransactionRequestDto request) {
        Account account = validateAccountAccess(userEmail, accountNumber);
        
        // Process deposit
        account.setBalance(account.getBalance().add(request.getAmount()));
        Account updatedAccount = accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = createTransactionRecord(
            account, 
            null,
            request.getAmount(), 
            TransactionType.DEPOSIT,
            request.getDescription()
        );

        return convertToDto(transaction, updatedAccount.getBalance());
    }

    @Transactional
    public TransactionResponseDto withdraw(String userEmail, String accountNumber, TransactionRequestDto request) {
        Account account = validateAccountAccess(userEmail, accountNumber);
        
        // Validate sufficient balance
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        // Process withdrawal
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        Account updatedAccount = accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = createTransactionRecord(
            account, 
            null,
            request.getAmount(), 
            TransactionType.WITHDRAWAL,
            request.getDescription()
        );

        return convertToDto(transaction, updatedAccount.getBalance());
    }

    @Transactional
    public TransactionResponseDto transfer(String userEmail, String sourceAccountNumber, 
                                         TransactionRequestDto request) {
        if (request.getDestinationAccountNumber() == null) {
            throw new IllegalArgumentException("Destination account number is required");
        }

        if (sourceAccountNumber.equals(request.getDestinationAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Validate source account
        Account sourceAccount = validateAccountAccess(userEmail, sourceAccountNumber);

        // Get destination account
        Account destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("Destination account not found"));

        if (!destinationAccount.isActive()) {
            throw new IllegalArgumentException("Destination account is not active");
        }

        // Check sufficient balance
        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds for transfer");
        }

        // Process transfer
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));

        // Save both accounts
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Create transaction record
        Transaction transaction = createTransactionRecord(
            sourceAccount,
            destinationAccount,
            request.getAmount(),
            TransactionType.TRANSFER,
            request.getDescription()
        );

        // Send email notifications
        emailService.sendTransferNotification(
            sourceAccount.getUser(),
            destinationAccount.getUser(),
            transaction,
            sourceAccount.getBalance()
        );

        return convertToDto(transaction, sourceAccount.getBalance());
    }

    public Page<TransactionResponseDto> getAccountTransactions(String userEmail, 
                                                             String accountNumber, 
                                                             Pageable pageable) {
        Account account = validateAccountAccess(userEmail, accountNumber);
        
        Page<Transaction> transactions = transactionRepository
            .findByAccountOrderByTimestampDesc(account, pageable);
            
        return transactions.map(transaction -> 
            convertToDto(transaction, account.getBalance()));
    }

    private Account validateAccountAccess(String userEmail, String accountNumber) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
            
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            
        if (!account.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Account doesn't belong to the user");
        }
        
        if (!account.isActive()) {
            throw new IllegalArgumentException("Account is not active");
        }
        
        return account;
    }

    private Transaction createTransactionRecord(Account sourceAccount, 
                                              Account destinationAccount,
                                              BigDecimal amount, 
                                              TransactionType type,
                                              String description) {
        Transaction transaction = new Transaction();
        transaction.setAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setReferenceNumber(generateReferenceNumber());
        
        return transactionRepository.save(transaction);
    }

    private String generateReferenceNumber() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private TransactionResponseDto convertToDto(Transaction transaction, BigDecimal balanceAfterTransaction) {
        return new TransactionResponseDto(
            transaction.getId(),
            transaction.getReferenceNumber(),
            transaction.getAmount(),
            transaction.getType(),
            transaction.getDescription(),
            transaction.getTimestamp(),
            transaction.getAccount().getAccountNumber(),
            transaction.getDestinationAccount() != null ? 
                transaction.getDestinationAccount().getAccountNumber() : null,
            balanceAfterTransaction
        );
    }
}