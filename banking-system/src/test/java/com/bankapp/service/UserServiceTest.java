package com.bankapp.service;

import com.bankapp.dto.UserRegistrationDto;
import com.bankapp.dto.UserResponseDto;
import com.bankapp.model.User;
import com.bankapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto registrationDto;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Setup test data
        registrationDto = new UserRegistrationDto();
        registrationDto.setFirstName("John");
        registrationDto.setLastName("Doe");
        registrationDto.setEmail("john@example.com");
        registrationDto.setPassword("password123");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("encoded_password");
        mockUser.setEnabled(true);
        mockUser.setRoles(new HashSet<>());
    }

    @Test
    void registerUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        UserResponseDto result = userService.registerUser(registrationDto);

        // Assert
        assertNotNull(result);
        assertEquals(registrationDto.getEmail(), result.getEmail());
        assertEquals(registrationDto.getFirstName(), result.getFirstName());
        assertEquals(registrationDto.getLastName(), result.getLastName());
    }

    @Test
    void registerUser_DuplicateEmail() {
        // Arrange
        when(userRepository.existsByEmail(any())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(registrationDto);
        });
    }

    @Test
    void getUserByEmail_Success() {
        // Arrange
        when(userRepository.findByEmail("john@example.com"))
            .thenReturn(Optional.of(mockUser));

        // Act
        UserResponseDto result = userService.getUserByEmail("john@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());
        assertEquals(mockUser.getFirstName(), result.getFirstName());
    }

    @Test
    void getUserByEmail_NotFound() {
        // Arrange
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            userService.getUserByEmail("nonexistent@example.com");
        });
    }
}