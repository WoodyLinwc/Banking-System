package com.bankapp.service;

import com.bankapp.dto.AdminDashboardDto;
import com.bankapp.dto.UserStatusUpdateDto;
import com.bankapp.model.User;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AdminService(UserRepository userRepository, 
                       AccountRepository accountRepository,
                       TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AdminDashboardDto getDashboardStats() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        
        return new AdminDashboardDto(
            userRepository.count(),
            accountRepository.count(),
            accountRepository.sumTotalBalance(),
            transactionRepository.count(),
            userRepository.countByEnabled(true),
            userRepository.countByCreatedAtAfter(today)
        );
    }

    @Transactional
    public void updateUserStatus(Long userId, UserStatusUpdateDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        user.setEnabled(updateDto.isEnabled());
        if (updateDto.getRoles() != null) {
            user.setRoles(new HashSet<>(updateDto.getRoles()));
        }
        
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public long getActiveUsers() {
        return userRepository.countByEnabled(true);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalSystemBalance() {
        return accountRepository.sumTotalBalance();
    }

    @Transactional(readOnly = true)
    public long getTotalTransactions() {
        return transactionRepository.count();
    }

    @Transactional(readOnly = true)
    public long getNewUsersToday() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return userRepository.countByCreatedAtAfter(today);
    }
}