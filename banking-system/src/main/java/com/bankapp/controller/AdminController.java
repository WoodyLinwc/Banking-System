package com.bankapp.controller;

import com.bankapp.dto.AdminDashboardDto;
import com.bankapp.dto.UserResponseDto;
import com.bankapp.dto.UserStatusUpdateDto;
import com.bankapp.service.AdminService;
import com.bankapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")  // Requires admin role for all endpoints
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getDashboardStats() {
        AdminDashboardDto stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody UserStatusUpdateDto updateDto) {
        adminService.updateUserStatus(userId, updateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/users")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(adminService.getTotalUsers());
    }

    @GetMapping("/stats/users/active")
    public ResponseEntity<Long> getActiveUsers() {
        return ResponseEntity.ok(adminService.getActiveUsers());
    }

    @GetMapping("/stats/balance")
    public ResponseEntity<Long> getTotalSystemBalance() {
        return ResponseEntity.ok(adminService.getTotalSystemBalance().longValue());
    }

    @GetMapping("/stats/transactions")
    public ResponseEntity<Long> getTotalTransactions() {
        return ResponseEntity.ok(adminService.getTotalTransactions());
    }

    @GetMapping("/stats/users/new")
    public ResponseEntity<Long> getNewUsersToday() {
        return ResponseEntity.ok(adminService.getNewUsersToday());
    }
}