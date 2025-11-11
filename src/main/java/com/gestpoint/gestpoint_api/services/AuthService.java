/**
 * @author falvesmac
 */

package com.gestpoint.gestpoint_api.services;

import com.gestpoint.gestpoint_api.domain.User;
import com.gestpoint.gestpoint_api.dto.LoginRequestDTO;
import com.gestpoint.gestpoint_api.dto.RegisterRequestDTO;
import com.gestpoint.gestpoint_api.dto.ResponseDTO;
import com.gestpoint.gestpoint_api.infra.security.TokenService;
import com.gestpoint.gestpoint_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDTO login(LoginRequestDTO body) {
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (passwordEncoder.matches(body.password(), user.getPasswordHash())) {
            String token = tokenService.generateToken(user);
            return new ResponseDTO(user.getName(), token);
        }

        throw new BadCredentialsException("Invalid credentials");
    }

    public ResponseDTO register(RegisterRequestDTO body) {

        this.userRepository.findByEmail(body.email()).ifPresent(user -> {;
            throw new IllegalArgumentException("User already exists");
        });

        User newUser = new User();
        newUser.setEmail(body.email());
        newUser.setName(body.name());
        newUser.setPasswordHash(passwordEncoder.encode(body.password()));
        newUser.setRole("USER");

        this.userRepository.save(newUser);

        String token = this.tokenService.generateToken(newUser);
        return new ResponseDTO(newUser.getName(), token);
    }
}