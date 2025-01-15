package com.bankapp.repository;

import com.bankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByEnabled(boolean enabled);
    long countByCreatedAtAfter(LocalDateTime date);
    
    @Query("SELECT u FROM User u WHERE u.roles LIKE %?1%")
    java.util.List<User> findByRole(String role);
}