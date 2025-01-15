package com.bankapp.repository;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    
    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a")
    BigDecimal sumTotalBalance();
    
    @Query("SELECT a FROM Account a WHERE a.balance < ?1")
    List<Account> findAccountsWithBalanceBelow(BigDecimal amount);
}