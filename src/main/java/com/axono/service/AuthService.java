package com.axono.service;

import com.axono.domain.User;
import com.axono.dto.LoginRequest;
import com.axono.dto.LoginResponse;
import com.axono.repository.UserRepository;
import com.axono.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse.UserResponse getCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return LoginResponse.UserResponse.from(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = findUserByIdentifier(request.identifier())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new LoginResponse(token, LoginResponse.UserResponse.from(user));
    }

    private java.util.Optional<User> findUserByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) return java.util.Optional.empty();

        // Tenta por email
        var byEmail = userRepository.findByEmailIgnoreCase(identifier.trim());
        if (byEmail.isPresent()) return byEmail;

        // Tenta por matrícula
        var byMatricula = userRepository.findByMatricula(identifier.trim());
        if (byMatricula.isPresent()) return byMatricula;

        // Tenta por CRM
        return userRepository.findByCrm(identifier.trim());
    }
}
