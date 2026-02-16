package com.axono.dto;

import com.axono.domain.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record LoginResponse(
        String token,
        UserResponse user
) {
    public record UserResponse(
            UUID id,
            String name,
            String email,
            List<String> roles
    ) {
        public static UserResponse from(User user) {
            List<String> roleNames = user.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    roleNames
            );
        }
    }
}
