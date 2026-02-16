package com.axono.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Usuário do sistema (aluno, preceptor, coordenação ou diretor).
 * Pode possuir mais de uma permissão (role).
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_matricula", columnList = "matricula"),
        @Index(name = "idx_users_crm", columnList = "crm")
})
@Getter
@Setter
public class User extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private List<UserRole> roles = new ArrayList<>();

    @Column(length = 50)
    private String matricula;

    @Column(length = 20)
    private String crm;

    /**
     * Indica se o usuário ainda precisa completar o fluxo de primeiro acesso
     * (definir senha, confirmar dados). Quando true, redireciona para /primeiro-acesso.
     */
    @Column(name = "first_access_pending", nullable = false)
    private boolean firstAccessPending = true;

    public void setRoles(List<UserRole> roles) {
        this.roles = roles != null ? new ArrayList<>(roles) : new ArrayList<>();
    }

    public void addRole(UserRole role) {
        if (role != null && !this.roles.contains(role)) {
            this.roles.add(role);
        }
    }

    public boolean hasRole(UserRole role) {
        return roles != null && roles.contains(role);
    }

    public enum UserRole {
        ALUNO,
        PRECEPTOR,
        COORDENACAO,
        DIRETOR
    }
}
