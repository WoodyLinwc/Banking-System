package com.bankapp.controller;

import com.bankapp.dto.*;
import com.bankapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserResponseDto user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateCurrentUser(
            @Valid @RequestBody UserUpdateDto updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserResponseDto updatedUser = userService.updateUser(authentication.getName(), updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUserByEmail(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.changePassword(authentication.getName(), passwordChangeDto);
        return ResponseEntity.ok().build();
    }
}