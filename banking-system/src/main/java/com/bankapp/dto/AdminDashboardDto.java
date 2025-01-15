package com.bankapp.dto;

import java.math.BigDecimal;

public class AdminDashboardDto {
    private long totalUsers;
    private long totalAccounts;
    private BigDecimal totalBalance;
    private long totalTransactions;
    private long activeUsers;
    private long newUsersToday;

    // Constructors
    public AdminDashboardDto() {
    }

    public AdminDashboardDto(long totalUsers, long totalAccounts, BigDecimal totalBalance,
                           long totalTransactions, long activeUsers, long newUsersToday) {
        this.totalUsers = totalUsers;
        this.totalAccounts = totalAccounts;
        this.totalBalance = totalBalance;
        this.totalTransactions = totalTransactions;
        this.activeUsers = activeUsers;
        this.newUsersToday = newUsersToday;
    }

    // Getters and Setters
    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(long totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public long getNewUsersToday() {
        return newUsersToday;
    }

    public void setNewUsersToday(long newUsersToday) {
        this.newUsersToday = newUsersToday;
    }
}