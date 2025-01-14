package com.bankapp.repository;

import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccount(Account account, Pageable pageable);
    Page<Transaction> findByAccountOrderByTimestampDesc(Account account, Pageable pageable);
}