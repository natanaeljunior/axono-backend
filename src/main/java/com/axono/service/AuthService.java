package com.axono.service;

import com.axono.domain.User;
import com.axono.dto.FirstAccessCompleteRequest;
import com.axono.dto.LoginRequest;
import com.axono.dto.LoginResponse;
import com.axono.exception.BusinessException;
import com.axono.repository.UserRepository;
import com.axono.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String PASSWORD_RULES = "A senha deve ter no mínimo 8 caracteres, uma letra maiúscula e um número ou caractere especial.";

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

    /**
     * Conclui o primeiro acesso: valida a nova senha, atualiza o hash e marca firstAccessPending = false.
     */
    @Transactional
    public LoginResponse.UserResponse completeFirstAccess(UUID userId, FirstAccessCompleteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        if (!user.isFirstAccessPending()) {
            throw new BusinessException("Primeiro acesso já foi concluído.");
        }
        validateFirstAccessPassword(request.newPassword());
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setFirstAccessPending(false);
        user = userRepository.save(user);
        return LoginResponse.UserResponse.from(user);
    }

    private void validateFirstAccessPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new BusinessException(PASSWORD_RULES);
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new BusinessException(PASSWORD_RULES);
        }
        if (!password.matches(".*[0-9].*") && !password.matches(".*[^A-Za-z0-9].*")) {
            throw new BusinessException(PASSWORD_RULES);
        }
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
