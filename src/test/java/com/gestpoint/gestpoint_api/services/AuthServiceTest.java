package com.gestpoint.gestpoint_api.services;

import com.gestpoint.gestpoint_api.domain.User;
import com.gestpoint.gestpoint_api.dto.LoginRequestDTO;
import com.gestpoint.gestpoint_api.dto.RegisterRequestDTO;
import com.gestpoint.gestpoint_api.dto.ResponseDTO;
import com.gestpoint.gestpoint_api.infra.security.TokenService;
import com.gestpoint.gestpoint_api.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should authenticate user with valid credentials")
    void loginCase01() {
        User existingUser = buildUser(1L, "test@test.com", "hashed_password_ok");
        LoginRequestDTO validLogin = new LoginRequestDTO("test@test.com", "correct_password");

        when(userRepository.findByEmail(validLogin.email())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(validLogin.password(), existingUser.getPasswordHash())).thenReturn(true);
        when(tokenService.generateToken(existingUser)).thenReturn("fake_jwt_token");

        ResponseDTO response = authService.login(validLogin);

        assertThat(response.token()).isEqualTo("fake_jwt_token");
        verify(tokenService, times(1)).generateToken(existingUser);

    }

    @Test
    @DisplayName("Should not authenticate user with invalid credentials")
    void loginCase02() {
        User existingUser = buildUser(1L, "test@test.com", "hashed_password_ok");
        LoginRequestDTO invalidLogin = new LoginRequestDTO("test@test.com", "incorrect_password");

        when(userRepository.findByEmail(invalidLogin.email())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(invalidLogin);
        });

        verify(tokenService, never()).generateToken(existingUser);
    }

    @Test
    @DisplayName("Should register a new user with valid data")
    void registerCase01() {
        RegisterRequestDTO validRegister = new RegisterRequestDTO("Test User", "usertest@mail.com", "new_password");

        when(userRepository.findByEmail(validRegister.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validRegister.password())).thenReturn("hashed_new_password");
        when(tokenService.generateToken(any(User.class))).thenReturn("new_user_jwt");

        ResponseDTO response = authService.register(validRegister);

        assertThat(response.token()).isEqualTo("new_user_jwt");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findByEmail(validRegister.email());
    }

    @Test
    @DisplayName("Should not register a new user with existing email")
    void registerCase02() {
        RegisterRequestDTO duplicateRegister = new RegisterRequestDTO("Test User Fail", "usertest@mail.com", "new_password");

        User existingUser = buildUser(1L, duplicateRegister.email(), "hashed_new_password");

        when(userRepository.findByEmail(duplicateRegister.email())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(duplicateRegister);
        });

        verify(userRepository, never()).save(any(User.class));
        verify(tokenService, never()).generateToken(any(User.class));

    }

    private User buildUser(Long id, String email, String passwordHash) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setName("Test User");
        user.setRole("USER");
        user.setTenantId(1L);
        return user;
    }

}