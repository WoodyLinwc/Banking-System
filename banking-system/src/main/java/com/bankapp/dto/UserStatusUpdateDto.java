package com.bankapp.dto;

import java.util.List;

public class UserStatusUpdateDto {
    private boolean enabled;
    private List<String> roles;

    // Constructors
    public UserStatusUpdateDto() {
    }

    public UserStatusUpdateDto(boolean enabled, List<String> roles) {
        this.enabled = enabled;
        this.roles = roles;
    }

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}