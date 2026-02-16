package com.axono.controller;

import com.axono.dto.FirstAccessCompleteRequest;
import com.axono.dto.LoginRequest;
import com.axono.dto.LoginResponse;
import com.axono.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse.UserResponse> me(Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(authService.getCurrentUser(userId));
    }

    /**
     * Conclui o primeiro acesso do usuário autenticado: define a nova senha e marca o primeiro acesso como concluído.
     */
    @PostMapping("/complete-first-access")
    public ResponseEntity<LoginResponse.UserResponse> completeFirstAccess(
            Authentication authentication,
            @Valid @RequestBody FirstAccessCompleteRequest request) {
        UUID userId = (UUID) authentication.getPrincipal();
        LoginResponse.UserResponse user = authService.completeFirstAccess(userId, request);
        return ResponseEntity.ok(user);
    }
}
